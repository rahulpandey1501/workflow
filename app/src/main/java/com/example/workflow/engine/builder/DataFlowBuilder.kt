package com.example.workflow.engine.builder

import com.example.workflow.engine.Utils
import com.example.workflow.engine.node.Data
import com.example.workflow.engine.dataflow.DataFlowExecutor
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.engine.dataflow.DataFlowManagerImpl
import com.example.workflow.engine.helper.DataFlowValidator
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
        sanity(dataHolderHelper.getAllNodes())
        return dataFlowManager
    }

    private fun generateNodeNavigator(dataManagerHelper: DataManagerHelper) {

        dataManagerHelper.getAllNodes().forEach { childNode ->

            // iterate over all Node
            val consumers = Utils.getNodeConsumers(childNode)

            consumers.forEach { nodeClass ->
                val parentNode = dataManagerHelper.getNode(nodeClass)

                // build NodeNavigator
                childNode.getNodeContract().addIncoming(parentNode)
                parentNode.getNodeContract().addOutgoing(childNode)
            }
        }
    }

    private fun sanity(nodes: MutableCollection<Node>) {
        Thread {
            DataFlowValidator().validate(nodes)
        }.start()
    }
}