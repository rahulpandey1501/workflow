package com.example.workflow.engine.dataflow

import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.node.NodeState

class DataFlowManagerImpl(
    private val dataFlowExecutor: DataFlowExecutor

) : DataFlowManager(dataFlowExecutor.dataNodeMappingHelper) {

    override fun execute(data: Data) {
        dataFlowExecutor.process(data)
    }

    override fun execute(nodeBuilder: NodeBuilder) {
        dataFlowExecutor.process(nodeBuilder)
    }

    override fun updateNodeState(newNodeState: NodeState, nodeBuilder: NodeBuilder) {

        val lastState = nodeBuilder.getNodeContract().getNodeState()
        nodeBuilder.getNodeContract().setNodeState(newNodeState)

        if (lastState != newNodeState) {
            // if state changes and previous state was WAITING (Async) then process the outgoing nodes explicitly
            if (lastState == NodeState.WAITING) {
                execute(nodeBuilder.getNodeContract().getNodeData())
            }
        }
    }

    override fun traceWorkFlowStatus() {
        dataFlowExecutor.traceException()
    }

    override fun traceWorkFlowStatus(nodeBuilder: NodeBuilder) {
        dataFlowExecutor.traceException(nodeBuilder)
    }
}