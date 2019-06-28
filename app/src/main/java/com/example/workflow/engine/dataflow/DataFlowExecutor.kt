package com.example.workflow.engine.dataflow

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.helper.DataManagerHelper
import com.example.workflow.engine.node.Data
import com.example.workflow.engine.node.NodeState
import com.example.workflow.engine.nodeprocessorcontract.NodeProcessorCallback
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class DataFlowExecutor(val dataManagerHelper: DataManagerHelper) {

    /**
        To process a particular node
     */
    fun process(node: Node) {
        processNodes(listOf(node))
    }

    /**
        Process the data, process the dependent nodes on data as the data changes (output of some node)
     */
    fun process(data: Data) {
        with(dataManagerHelper) {

            val node = getNode(data)

            if (node.isExternalNode()) {
                process(node)

            } else {
                processNodes(node.getOutgoingNodes())
            }
        }
    }

    private fun processNodes(nodes: Collection<Node>) {

        val queue = ConcurrentLinkedQueue<Node>()
        queue.addAll(nodes.distinct())

        while (queue.isNotEmpty()) {
            val nodeBuilder = queue.remove()

            with(nodeBuilder) {
                val nodeContract = getNodeContract()
                val lastState = nodeContract.getNodeState()

                if (shouldProcessNode(nodeBuilder)) {
                    process(NodeProcessorCallback(this@DataFlowExecutor, nodeBuilder))
                    queue.addAll(getOutgoingNodes())

                } else if (shouldInvalidateDependantNodes(nodeBuilder, lastState)) {
                    propagateNodeState(nodeBuilder, NodeState.INVALID)
                }

                if (nodeBuilder.isTargetNode() && nodeContract.getNodeState() == NodeState.VALID) {
                    Log.d("Workflow", "Workflow completed with the result ${nodeContract.getNodeData()}")
                }
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

    private fun shouldInvalidateDependantNodes(node: Node, nodeState: NodeState): Boolean {
        return !shouldProcessNode(node) && nodeState == NodeState.VALID
    }

    fun trackWorkFlow(traceNode: Node? = null) {

        val visitedNode = mutableSetOf<Node>()
        val givenNode = traceNode ?: dataManagerHelper.getTargetNode()
        val queue = LinkedList<Node>()
        queue.add(givenNode)
        visitedNode.add(givenNode)

        while (queue.isNotEmpty()) {
            val nodeBuilder = queue.remove()

            Log.d(
                "workflow", "Node: ${Utils.getNodeId(nodeBuilder)} " +
                        "| Status: ${nodeBuilder.getNodeContract().getNodeState()} | ${nodeBuilder.getNodeContract().getNodeMessage()}"
            )

            queue.addAll(nodeBuilder.getIncomingNodes().minus(visitedNode.toList()))
            visitedNode.addAll(nodeBuilder.getIncomingNodes())
        }
    }

    private fun propagateNodeState(parentNode: Node, nodeState: NodeState) {
        val queue = ConcurrentLinkedQueue<Node>()
        val nodeBuilderSet = hashSetOf<Node>()
        queue.add(parentNode)
        nodeBuilderSet.add(parentNode)

        while (queue.isNotEmpty()) {
            val nodeBuilder = queue.remove()
            nodeBuilderSet.addAll(nodeBuilder.getOutgoingNodes())
            queue.addAll(nodeBuilder.getOutgoingNodes())
        }

        nodeBuilderSet.forEach {
            it.getNodeContract().setNodeState(nodeState)
            it.onStatusUpdated(nodeState, it.getNodeContract().getNodeMeta())
        }
    }
}