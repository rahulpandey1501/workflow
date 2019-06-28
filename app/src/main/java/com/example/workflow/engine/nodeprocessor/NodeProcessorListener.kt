package com.example.workflow.engine.nodeprocessor

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.node.NodeState
import java.util.concurrent.ConcurrentLinkedQueue

class NodeProcessorListener(private val dataFlowExecutor: DataFlowExecutor) {

    @Synchronized
    fun updateNodeStatus(node: Node, nodeState: NodeState, message: String?) {

        logNode(nodeState, node)

        val lastState = node.getNodeContract().getNodeState()

        node.getNodeContract().setNodeStateMessage(message)
        node.onStatusUpdated(nodeState, node.getNodeMeta())
        node.getNodeContract().setNodeState(nodeState)

        if (lastState != nodeState) {
            // if state changes and previous state was WAITING (Async) then process the outgoing nodes explicitly
            if (lastState == NodeState.WAITING) {
                dataFlowExecutor.process(node.getNodeContract().getNodeData())
            }
        }
    }

    fun propagateNodeStatus(parentNode: Node, nodeState: NodeState) {
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
            logNode(nodeState, it)
        }
    }


    private fun logNode(newNodeState: NodeState, node: Node) {
        Log.d(
            "workflow",
            "Process: ${Utils.getNodeId(node)} | previous: ${node.getNodeContract().getNodeState()}  Current: $newNodeState"
        )
    }
}