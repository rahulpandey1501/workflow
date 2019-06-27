package com.example.workflow.engine.node

import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.nodeprocessorcontract.NodeProcessorCallback

@Suppress("UNCHECKED_CAST")
abstract class Node {

    private lateinit var nodeDataContext: NodeDataContext
    private var isTargetNode: Boolean = false

    abstract fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta)

    abstract fun process(callback: NodeProcessorCallback)

    fun init(producer: Data?, isTargetNode: Boolean) {
        val node = NodeMeta(producer)
        val nodeNavigator = NodeNavigator()
        this.isTargetNode = isTargetNode
        this.nodeDataContext = NodeDataContext(node, nodeNavigator)
    }

    fun getId() = Utils.getName(this)

    fun getNodeContract(): NodeContract {
        return nodeDataContext
    }

    fun <T: Data> getResult(): T = getNodeMeta().result as T

    fun getNodeMeta(): NodeMeta = nodeDataContext.getNodeMeta()

    fun getIncomingNodes(): MutableCollection<Node> {
        return nodeDataContext.getIncomingNodes()
    }

    fun getOutgoingNode(): MutableCollection<Node> {
        return nodeDataContext.getOutgoingNodes()
    }

    fun isTargetNode() = isTargetNode

    open fun isExternalNode() = false
}