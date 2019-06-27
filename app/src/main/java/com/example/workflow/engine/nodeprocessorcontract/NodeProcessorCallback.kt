package com.example.workflow.engine.nodeprocessorcontract

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.node.NodeState

class NodeProcessorCallback(
    private val dataFlowExecutor: DataFlowExecutor,
    private val node: Node
) : INodeProcessContract {

    @Synchronized
    override fun updateNodeStatus(nodeState: NodeState, message: String?) {

        log(nodeState, node)

        node.getNodeContract().setNodeStateMessage(message)
        node.onStatusUpdated(nodeState, node.getNodeMeta())
        updateNodeState(nodeState, node)
    }

    @Synchronized
    private fun updateNodeState(newNodeState: NodeState, node: Node) {

        val lastState = node.getNodeContract().getNodeState()
        node.getNodeContract().setNodeState(newNodeState)

        if (lastState != newNodeState) {
            // if state changes and previous state was WAITING (Async) then process the outgoing nodes explicitly
            if (lastState == NodeState.WAITING) {
                dataFlowExecutor.process(node.getNodeContract().getNodeData())
            }
        }
    }

    private fun log(newNodeState: NodeState, node: Node) {
        Log.d(
            "workflow",
            "Process: ${Utils.getNodeId(node)} | previous: ${node.getNodeContract().getNodeState()}  Current: $newNodeState"
        )
    }
}