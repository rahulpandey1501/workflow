package com.example.workflow.engine.dataflow

import com.example.workflow.engine.node.Data
import com.example.workflow.engine.node.Node

abstract class DataFlowManager {

    abstract fun execute(data: Data)
    abstract fun execute(node: Node)
    abstract fun traceWorkFlowStatus()
    abstract fun traceWorkFlowStatus(node: Node)
}