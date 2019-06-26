package com.example.workflow.engine.builder

import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.node.*
import com.example.workflow.engine.nodeprocessorcontract.NodeProcessorCallback

@Suppress("UNCHECKED_CAST")
abstract class NodeBuilder {

    private lateinit var nodeDataContext: NodeDataContext

    abstract fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta)

    abstract fun process(callback: NodeProcessorCallback)

    fun init(producer: Data?) {
        val node = NodeMeta(producer)
        val nodeNavigator = NodeNavigator()
        this.nodeDataContext = NodeDataContext(node, nodeNavigator)
    }

    fun getNodeContract(): NodeContract {
        return nodeDataContext
    }

    fun <T: Data> getResult(): T = getNodeMeta().result as T

    fun getNodeMeta(): NodeMeta = nodeDataContext.getNodeMeta()

    fun getIncomingNodes(): MutableCollection<NodeBuilder> {
        return nodeDataContext.getIncomingNodes()
    }

    fun getOutgoingNode(): MutableCollection<NodeBuilder> {
        return nodeDataContext.getOutgoingNodes()
    }

    fun isResultNode(): Boolean {
        return nodeDataContext.getOutgoingNodes().isEmpty()
    }

}