package com.example.workflow.engine.dataflow

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.helper.DataManagerHelper
import com.example.workflow.engine.node.Data
import com.example.workflow.engine.node.NodeState
import com.example.workflow.engine.nodeprocessor.NodeProcessorListener
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class DataFlowExecutor(private val dataManager: DataManagerHelper) {

    private val nodeProcessCallback = NodeProcessorListener(this@DataFlowExecutor)

    /**
    Process the data, process the dependent nodes on data as the data changes (output of some node)
     */
    fun process(data: Data) {
        val node = dataManager.getNode(data)
        if (node.isExternalNode()) {
            processNode(node)

        } else {
            processNodes(node.getOutgoingNodes())
        }
    }

    /**
    To process a particular node
     */
    fun processNode(node: Node) {
        processNodes(listOf(node))
    }

    @Synchronized
    private fun processNodes(nodes: Collection<Node>) {

        val queue = ConcurrentLinkedQueue<Node>()
        queue.addAll(nodes.distinct())

        while (queue.isNotEmpty()) {
            val node = queue.remove()
            val nodeContract = node.getNodeContract()


            if (shouldProcessNode(node)) {
                node.process { nodeState: NodeState, message: String? ->
                    nodeProcessCallback.updateNodeStatus(node, nodeState, message)
                }
                queue.addAll(node.getOutgoingNodes())

            } else if (nodeContract.getNodeState() != NodeState.INVALID) {
                Log.d("workflow: state", node.getId())
                nodeProcessCallback.updateNodeStatus(node, NodeState.INVALID, nodeContract.getNodeMessage())
            }

            if (node.isTargetNode() && nodeContract.getNodeState() == NodeState.VALID) {
                Log.d("Workflow", "Workflow completed with the result ${nodeContract.getNodeData()}")
            }
        }
    }

    private fun shouldProcessNode(node: Node): Boolean {
        var propagation = true

        // check for valid incoming inputs
        node.getIncomingNodes().forEach {
            if (it.getNodeContract().getNodeState() != NodeState.VALID) {
                propagation = false
                return@forEach
            }
        }

        return if (node.isExternalNode()) true else propagation
    }

    fun trackWorkFlow(traceNode: Node? = null) {

        val visitedNode = mutableSetOf<Node>()
        val givenNode = traceNode ?: dataManager.getTargetNode()
        val queue = LinkedList<Node>()
        queue.add(givenNode)
        visitedNode.add(givenNode)

        while (queue.isNotEmpty()) {
            val nodeBuilder = queue.remove()

            Log.d(
                "workflow: trace", "Node: ${Utils.getNodeId(nodeBuilder)} " +
                        "| Status: ${nodeBuilder.getNodeContract().getNodeState()} | ${nodeBuilder.getNodeContract().getNodeMessage()}"
            )

            queue.addAll(nodeBuilder.getIncomingNodes().minus(visitedNode.toList()))
            visitedNode.addAll(nodeBuilder.getIncomingNodes())
        }
    }
}