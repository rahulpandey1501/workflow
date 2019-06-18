package com.example.workflow.engine.dataflow

import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.helper.DataNodeMappingHelper
import com.example.workflow.engine.node.NodeState

abstract class DataFlowManager(var dataNodeMappingHelper: DataNodeMappingHelper) {

    abstract fun execute(data: Data)
    abstract fun execute(nodeBuilder: NodeBuilder)

    fun updateNodeState(newNodeState: NodeState, nodeBuilder: NodeBuilder) {

        when (newNodeState) {

            NodeState.VALID -> publishResult(newNodeState, nodeBuilder)

            else -> if (nodeBuilder.getNodeContract().getNodeState() == NodeState.VALID) {
                publishResult(newNodeState, nodeBuilder)
            }
        }
    }

    private fun publishResult(nodeState: NodeState, nodeBuilder: NodeBuilder) {
        execute(nodeBuilder)
        nodeBuilder.getNodeContract().setNodeState(nodeState)
    }
}