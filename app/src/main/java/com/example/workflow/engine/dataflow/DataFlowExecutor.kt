package com.example.workflow.engine.dataflow

import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.helper.DataNodeMappingHelper

class DataFlowExecutor(val dataNodeMappingHelper: DataNodeMappingHelper) {

    private fun processNode(nodeBuilder: NodeBuilder) {
        nodeBuilder.process(nodeBuilder.getNodeContract().getNodeData())
    }

    // if input is data, process dependent nodes
    fun process(data: Data) {
        dataNodeMappingHelper.getOutgoingNodes(data)?.let {
            for (nodeBuilder in it) {
                processNode(nodeBuilder)
            }
        }
    }

    // process particular node
    fun process(processNodeBuilder: NodeBuilder) {
        dataNodeMappingHelper.getOutgoingNodes(processNodeBuilder)?.let {
            for (nodeBuilder in it) {
                processNode(nodeBuilder)
            }
        }
    }

//    private fun process(data: Data, nodeState: NodeState) {
//        val outgoingNodes = dataNodeMappingHelper[data.getId()]!!
//        val queue = ConcurrentLinkedQueue<NodeBuilder>()
//        queue.addAll(outgoingNodes.distinct())
//
//        while (queue.isNotEmpty()) {
//            val nodeBuilder = queue.remove()
//            processNodeState(nodeBuilder, nodeState)
//            queue.addAll(nodeBuilder.getNodeContract().getOutgoingNodes())
//        }
//    }
}