package com.example.workflow.engine.nodeprocessorcontract

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.node.NodeState
import kotlin.reflect.KClass

class NodeProcessorCallback(private val dataFlowExecutor: DataFlowExecutor) : INodeProcessContract {

    override fun <T : Data> getData(clazz: KClass<T>): T? {
        return dataFlowExecutor.dataManagerHelper.getNodeData(clazz)
    }

    @Synchronized
    override fun updateNodeStatus(node: Node, nodeState: NodeState, message: String?) {

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
            "Process: ${Utils.getName(javaClass)} | previous: ${node.getNodeContract().getNodeState()}  Current: $newNodeState"
        )
    }
}