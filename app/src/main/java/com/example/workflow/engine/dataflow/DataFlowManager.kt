package com.example.workflow.engine.dataflow

import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.node.NodeState

abstract class DataFlowManager {

    abstract fun execute(data: Data)
    abstract fun execute(nodeBuilder: NodeBuilder)
    abstract fun updateNodeState(newNodeState: NodeState, nodeBuilder: NodeBuilder)
    abstract fun traceWorkFlowStatus()
    abstract fun traceWorkFlowStatus(nodeBuilder: NodeBuilder)
}