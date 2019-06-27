package com.example.workflow.engine.builder

import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.engine.dataflow.DataFlowManagerImpl
import com.example.workflow.engine.exception.FlowException
import com.example.workflow.engine.exception.LoopDetectionException
import com.example.workflow.engine.helper.DataManagerHelper
import com.example.workflow.engine.node.ExternalNode
import com.example.workflow.engine.node.Node

// DataFlow
class DataFlowBuilder {

    private val dataHolderHelper = DataManagerHelper()

    // data flow helpers
    private var dataFlowExecutor = DataFlowExecutor(dataHolderHelper)
    private var dataFlowManager = DataFlowManagerImpl(dataFlowExecutor)

    /**
     * Register the required node to the flow builder
     */
    fun register(node: Node, produce: Data, isTargetNode: Boolean = false): DataFlowBuilder {
        node.init(produce, isTargetNode)
        dataHolderHelper.populate(node)
        return this
    }

    /**
     * Register the external nodes, external nodes can never be the target node
     */
    fun register(node: ExternalNode, data: Data): DataFlowBuilder {
        return register(node, data, false)
    }

    fun build(): DataFlowManager {
        generateNodeNavigator(dataHolderHelper)
        sanity(dataHolderHelper.getNodeMapping())
        return dataFlowManager
    }

    private fun generateNodeNavigator(dataManagerHelper: DataManagerHelper) {

        with(dataManagerHelper) {
            getNodeMapping().forEach {

                // iterate over all Node
                val childNode = it.value

                val consumers = Utils.getNodeConsumers(childNode)
                val produce = Utils.getNodeProduce(childNode)

                // getting all the possible outgoing nodes
                consumers.forEach { dataClass ->

                    val parentNode = getNode(dataClass.java)

                    // build NodeNavigator
                    childNode.getNodeContract().addIncoming(parentNode, dataClass)
                    parentNode.getNodeContract().addOutgoing(childNode, produce)
                }
            }
        }
    }

    private fun sanity(nodeMapping: MutableMap<String, Node>) {
        Thread {
            checkForLoop(nodeMapping)
            checkForOrphanNodes(nodeMapping)
            checkForTargetNode(nodeMapping)
        }.start()
    }

    private fun checkForOrphanNodes(mapping: MutableMap<String, Node>) {
        val orphanNode = mapping.filterValues { it.getIncomingNodes().isEmpty() && it.getOutgoingNode().isEmpty() }
            .values.firstOrNull()
        orphanNode?.let { throw FlowException("Workflow orphan node found Node: ${Utils.getName(orphanNode::class.java)}") }
    }

    /* Optional check, just to restrict to one final output */
    private fun checkForTargetNode(nodeMapping: MutableMap<String, Node>) {
        val targetNodes = nodeMapping.values.filter { it.isTargetNode() }
        val targetNodeCount = targetNodes.size

        if (targetNodeCount == 0) {
            throw FlowException("No target node found")

        } else if (targetNodeCount > 1) {
            val nodes = targetNodes.map { Utils.getName(it) }
            throw FlowException("More than one target node found $nodes")
        }
    }

    private fun checkForLoop(mapping: MutableMap<String, Node>) {
        mapping.values.forEach { nodeBuilder ->
            nodeBuilder.getOutgoingNode().forEach {
                if (it.getOutgoingNode().contains(nodeBuilder)) {
                    throw LoopDetectionException(it, nodeBuilder)
                }
            }
        }
    }
}