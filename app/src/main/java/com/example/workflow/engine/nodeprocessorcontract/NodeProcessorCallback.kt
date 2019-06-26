package com.example.workflow.engine.nodeprocessorcontract

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.node.NodeState
import kotlin.reflect.KClass

class NodeProcessorCallback(private val dataFlowExecutor: DataFlowExecutor) : INodeProcessContract {

    override fun <T : Data> getData(clazz: KClass<T>): T? {
        return dataFlowExecutor.dataManagerHelper.getNodeData(clazz)
    }

    @Synchronized
    override fun updateNodeStatus(nodeBuilder: NodeBuilder, nodeState: NodeState, message: String?) {

        log(nodeState, nodeBuilder)

        nodeBuilder.getNodeContract().setNodeStateMessage(message)
        nodeBuilder.onStatusUpdated(nodeState, nodeBuilder.getNodeMeta())
        updateNodeState(nodeState, nodeBuilder)
    }

    @Synchronized
    private fun updateNodeState(newNodeState: NodeState, nodeBuilder: NodeBuilder) {

        val lastState = nodeBuilder.getNodeContract().getNodeState()
        nodeBuilder.getNodeContract().setNodeState(newNodeState)

        if (lastState != newNodeState) {
            // if state changes and previous state was WAITING (Async) then process the outgoing nodes explicitly
            if (lastState == NodeState.WAITING) {
                dataFlowExecutor.process(nodeBuilder.getNodeContract().getNodeData())
            }
        }
    }

    private fun log(newNodeState: NodeState, nodeBuilder: NodeBuilder) {
        Log.d(
            "workflow",
            "Process: ${Utils.getName(javaClass)} | previous: ${nodeBuilder.getNodeContract().getNodeState()}  Current: $newNodeState"
        )
    }
}