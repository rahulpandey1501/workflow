package com.example.workflow.engine.dataflow

import com.example.workflow.engine.node.Data
import com.example.workflow.engine.node.Node

class DataFlowManagerImpl(
    private val dataFlowExecutor: DataFlowExecutor

) : DataFlowManager() {

    override fun execute(data: Data) {
        dataFlowExecutor.process(data)
    }

    override fun execute(node: Node) {
        dataFlowExecutor.processNode(node)
    }

    override fun traceWorkFlowStatus() {
        dataFlowExecutor.trackWorkFlow()
    }

    override fun traceWorkFlowStatus(node: Node) {
        dataFlowExecutor.trackWorkFlow(node)
    }
}