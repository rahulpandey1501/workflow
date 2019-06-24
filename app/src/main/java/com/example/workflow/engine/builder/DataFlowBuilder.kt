package com.example.workflow.engine.builder

import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.engine.dataflow.DataFlowManagerImpl
import com.example.workflow.engine.exception.FlowException
import com.example.workflow.engine.exception.LoopDetectionException
import com.example.workflow.engine.helper.DataManagerHelper

// DataFlow
class DataFlowBuilder {

    private val dataHolderHelper = DataManagerHelper()

    // data flow helpers
    var dataFlowExecutor = DataFlowExecutor(dataHolderHelper)
    private var dataFlowManager = DataFlowManagerImpl(dataFlowExecutor)

    fun register(nodeBuilder: NodeBuilder): DataFlowBuilder {
        nodeBuilder.init(dataFlowManager)
        dataHolderHelper.populate(nodeBuilder)
        return this
    }

    fun build(): DataFlowManager {
        generateNodeNavigator(dataHolderHelper)
        sanity(dataHolderHelper.getNodeMapping())
        return dataFlowManager
    }

    private fun generateNodeNavigator(dataManagerHelper: DataManagerHelper) {
        with(dataManagerHelper) {
            getNodeMapping().forEach {

                // iterate over all NodeBuilder
                val childNode = it.value

                with(Utils.getBuilderAnnotation(childNode)) {

                    // getting all the possible outgoing nodes
                    consumes.forEach { dataClass ->

                        val parentNode = getNode(dataClass)

                        // build NodeNavigator
                        childNode.getNodeContract().addIncoming(parentNode, dataClass)
                        parentNode?.getNodeContract()?.addOutgoing(childNode, produce)
                    }
                }
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
        val orphanNode = mapping.filterValues { it.getIncomingNodes().isEmpty() && it.getOutgoingNode().isEmpty() }
            .values.firstOrNull()
        orphanNode?.let { throw FlowException("Workflow orphan node found Node: ${Utils.getName(orphanNode::class.java)}") }
    }

    /* Optional check, just to restrict to one final output */
    private fun checkForResultNode(nodeBuilderMapping: MutableMap<String, NodeBuilder>) {
        val endNodes = nodeBuilderMapping.values.filter { it.getOutgoingNode().isNullOrEmpty() }
        val endNodesCount = endNodes.size

        if (endNodesCount == 0) {
            throw FlowException("No result node found")

        }
    }

    private fun checkForLoop(builderMapping: MutableMap<String, NodeBuilder>) {
        builderMapping.values.forEach { nodeBuilder ->
            nodeBuilder.getOutgoingNode().forEach {
                if (it.getOutgoingNode().contains(nodeBuilder)) {
                    throw LoopDetectionException(it, nodeBuilder)
                }
            }
        }
    }
}