package com.example.workflow.engine.dataflow

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.helper.DataManagerHelper
import com.example.workflow.engine.node.NodeState
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class DataFlowExecutor(val dataManagerHelper: DataManagerHelper) {

    fun process(nodeBuilder: NodeBuilder) {
        processNodeBuilders(nodeBuilder.getOutgoingNode())
    }

    fun process(data: Data) {
        dataManagerHelper.getOutgoingNodes(data)?.let { processNodeBuilders(it) }
    }

    private fun processNodeBuilders(outgoingNodes: Collection<NodeBuilder>) {

        val queue = ConcurrentLinkedQueue<NodeBuilder>()
        queue.addAll(outgoingNodes.distinct())

        while (queue.isNotEmpty()) {
            val nodeBuilder = queue.remove()
            val data = nodeBuilder.getNodeContract().getNodeData()
            val lastState = nodeBuilder.getNodeContract().getNodeState()

            if (shouldProcessNode(nodeBuilder)) {
                nodeBuilder.process(data)
                queue.addAll(nodeBuilder.getOutgoingNode())

            } else if (shouldInvalidateDependantNodes(nodeBuilder, lastState)) {
                propagateNodeState(nodeBuilder, NodeState.INVALID)
            }

            if (nodeBuilder.isResultNode() && nodeBuilder.getNodeContract().getNodeState() == NodeState.VALID) {
                Log.d("Workflow", "Workflow completed with the result ${nodeBuilder.getNodeContract().getNodeData()}")
                traceException()
            }
        }
    }

    private fun shouldProcessNode(nodeBuilder: NodeBuilder): Boolean {
        var propagation = true

        // check for valid incoming inputs

        nodeBuilder.getIncomingNodes().forEach {
            if (it.getNodeContract().getNodeState() != NodeState.VALID) {
                propagation = false
                return@forEach
            }
        }
        return propagation
    }

    private fun shouldInvalidateDependantNodes(nodeBuilder: NodeBuilder, nodeState: NodeState): Boolean {
        return !shouldProcessNode(nodeBuilder) && nodeState == NodeState.VALID
    }

    fun traceException(traceNodeBuilder: NodeBuilder? = null) {

        val visitedNode = mutableSetOf<NodeBuilder>()
        val givenNode = traceNodeBuilder ?: dataManagerHelper.getResultNode()
        val queue = LinkedList<NodeBuilder>()
        queue.add(givenNode)
        visitedNode.add(givenNode)

        while (queue.isNotEmpty()) {
            val nodeBuilder = queue.remove()

            Log.d(
                "Workflow", "Node: ${Utils.getName(nodeBuilder.javaClass)} " +
                        "| Status: ${nodeBuilder.getNodeContract().getNodeState()} | ${nodeBuilder.getNodeContract().getNodeMessage()}"
            )

            queue.addAll(nodeBuilder.getIncomingNodes().minus(visitedNode.toList()))
            visitedNode.addAll(nodeBuilder.getIncomingNodes())
        }
    }

    private fun propagateNodeState(parentNode: NodeBuilder, nodeState: NodeState) {
        val queue = ConcurrentLinkedQueue<NodeBuilder>()
        val nodeBuilderSet = hashSetOf<NodeBuilder>()
        queue.add(parentNode)
        nodeBuilderSet.add(parentNode)

        while (queue.isNotEmpty()) {
            val nodeBuilder = queue.remove()
            nodeBuilderSet.addAll(nodeBuilder.getOutgoingNode())
            queue.addAll(nodeBuilder.getOutgoingNode())
        }

        nodeBuilderSet.forEach {
            it.getNodeContract().setNodeState(nodeState)
            it.onStatusUpdated(nodeState, it.getNodeContract().getNodeMeta())
        }
    }
}