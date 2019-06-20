package com.example.workflow.engine.builder

import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.engine.dataflow.DataFlowManagerImpl
import com.example.workflow.engine.exception.FlowException
import com.example.workflow.engine.exception.LoopDetectionException
import com.example.workflow.engine.helper.DataNodeMappingHelper

// DataFlow
class DataFlowBuilder {

    private var nodeBuilderMapping: MutableMap<String, NodeBuilder> = hashMapOf() // node builder mapping
    private val dataNodeMappingHelper = DataNodeMappingHelper(nodeBuilderMapping)

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
        sanity(nodeBuilderMapping)
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

    private fun sanity(nodeBuilderMapping: MutableMap<String, NodeBuilder>) {
        Thread {
            checkForLoop(nodeBuilderMapping)
            checkForOrphanNodes(nodeBuilderMapping)
            checkForResultNode(nodeBuilderMapping)
        }.start()
    }

    private fun checkForOrphanNodes(mapping: MutableMap<String, NodeBuilder>) {
        val orphanNode = mapping.filterValues { it.getIncomingNodes().isEmpty() && it.getOutgoingNodes().isEmpty() }.values.firstOrNull()
        orphanNode?.let { throw FlowException("Workflow orphan node found Node: ${Utils.getName(orphanNode.javaClass)}") }
    }

    /* Optional check, just to restrict to one final output */
    private fun checkForResultNode(nodeBuilderMapping: MutableMap<String, NodeBuilder>) {
        val endNodes = nodeBuilderMapping.values.filter { it.getOutgoingNodes().isNullOrEmpty() }
        val endNodesCount = endNodes.size

        if (endNodesCount == 0) {
            throw FlowException("No result node found")

        }
//        else if (endNodesCount > 1) {
//            throw FlowException("Workflow contain only one result node")
//        }
    }

    private fun checkForLoop(builderMapping: MutableMap<String, NodeBuilder>) {
        builderMapping.values.forEach { nodeBuilder ->
            nodeBuilder.getOutgoingNodes().forEach {
                if (it.getOutgoingNodes().contains(nodeBuilder)) {
                    throw LoopDetectionException(it, nodeBuilder)
                }
            }
        }
    }
}