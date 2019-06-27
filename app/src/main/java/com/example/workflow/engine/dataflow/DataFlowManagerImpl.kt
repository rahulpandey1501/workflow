package com.example.workflow.engine.dataflow

import com.example.workflow.engine.node.Data
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.node.NodeState

class DataFlowManagerImpl(
    private val dataFlowExecutor: DataFlowExecutor

) : DataFlowManager() {

    override fun execute(data: Data) {
        dataFlowExecutor.process(data)
    }

    override fun execute(node: Node) {
        dataFlowExecutor.process(node)
    }

    override fun updateNodeState(newNodeState: NodeState, node: Node) {

        val lastState = node.getNodeContract().getNodeState()
        node.getNodeContract().setNodeState(newNodeState)

        if (lastState != newNodeState) {
            // if state changes and previous state was WAITING (Async) then process the outgoing nodes explicitly
            if (lastState == NodeState.WAITING) {
                execute(node.getNodeContract().getNodeData())
            }
        }
    }

    override fun traceWorkFlowStatus() {
        dataFlowExecutor.trackWorkFlow()
    }

    override fun traceWorkFlowStatus(node: Node) {
        dataFlowExecutor.trackWorkFlow(node)
    }
}