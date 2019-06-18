package com.example.workflow.engine.dataflow

import com.example.workflow.engine.builder.NodeBuilder

class DataFlowManagerImpl(
    private val dataFlowExecutor: DataFlowExecutor
) : DataFlowManager(dataFlowExecutor.dataNodeMappingHelper) {

    override fun execute(data: Data) {
        dataFlowExecutor.process(data)
    }

    override fun execute(nodeBuilder: NodeBuilder) {
        dataFlowExecutor.process(nodeBuilder)
    }
}