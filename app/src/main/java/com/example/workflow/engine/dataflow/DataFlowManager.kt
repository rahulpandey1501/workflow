package com.example.workflow.engine.dataflow

import com.example.workflow.engine.node.Node
import com.example.workflow.engine.node.NodeState

abstract class DataFlowManager {

    abstract fun execute(data: Data)
    abstract fun execute(node: Node)
    abstract fun updateNodeState(newNodeState: NodeState, node: Node)
    abstract fun traceWorkFlowStatus()
    abstract fun traceWorkFlowStatus(node: Node)
}