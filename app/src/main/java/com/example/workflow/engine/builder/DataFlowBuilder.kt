package com.example.workflow.engine.builder

import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.engine.dataflow.DataFlowManagerImpl
import com.example.workflow.engine.helper.DataNodeMappingHelper

// DataFlow
class DataFlowBuilder {

    // NodeBuilder mapping
    private var nodeBuilderMapping: MutableMap<String, NodeBuilder> = hashMapOf() // node builder mapping
    private val dataNodeMappingHelper = DataNodeMappingHelper()

    // data flow helpers
    var dataFlowExecutor = DataFlowExecutor(dataNodeMappingHelper)
    private var dataFlowManager = DataFlowManagerImpl(dataFlowExecutor)

    fun register(currentNode: NodeBuilder, producer: Data, consumer: Array<Data>): DataFlowBuilder {

        currentNode.init(producer, dataFlowManager)

        // add builders to map, using for generating the outgoing and incoming for a NodeBuilder
        nodeBuilderMapping[Utils.getName(currentNode.javaClass)] = currentNode

        // add dependency nodes mapping against data identifier
        consumer.forEach {
            dataNodeMappingHelper.put(it, currentNode)
        }

        return this
    }

    fun build(): DataFlowManager {
        generateNodeNavigator(nodeBuilderMapping)
        return dataFlowManager
    }

    private fun generateNodeNavigator(builderMapping: MutableMap<String, NodeBuilder>) {
        builderMapping.forEach {
            // iterate over all NodeBuilder

            val currentNodeBuilder = it.value

            // getting all the possible outgoing nodes
            Utils.getOutgoingNodeId(currentNodeBuilder).forEach { builderClass ->
                val nodeBuilder = builderMapping[Utils.getName(builderClass.java)]!!

                // build NodeNavigator
                currentNodeBuilder.getNodeContract().addOutgoing(nodeBuilder)
                nodeBuilder.getNodeContract().addIncoming(currentNodeBuilder)
            }
        }
    }
}