package com.example.workflow.engine.node

import com.example.workflow.engine.Utils
import com.example.workflow.engine.exception.IllegalSelectionException
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class Node {

    private lateinit var nodeDataContext: NodeDataContext
    private var isTargetNode: Boolean = false

    abstract fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta)

    abstract fun process(callback: (NodeState, String?) -> Unit)

    fun init(producer: Data?, isTargetNode: Boolean) {
        val node = NodeMeta(producer)
        val nodeNavigator = NodeNavigator()
        this.isTargetNode = isTargetNode
        this.nodeDataContext = NodeDataContext(node, nodeNavigator)
    }

    fun getId() = Utils.getNodeId(this)

    fun getNodeContract(): NodeContract {
        return nodeDataContext
    }

    fun <T : Data> getData(): T = getNodeMeta().result as T

    fun getNodeMeta(): NodeMeta = nodeDataContext.getNodeMeta()

    fun getIncomingNodes(): MutableCollection<Node> {
        return nodeDataContext.getIncomingNodes().values
    }

    fun getOutgoingNodes(): MutableCollection<Node> {
        return nodeDataContext.getOutgoingNodes().values
    }

    fun isTargetNode() = isTargetNode

    open fun isExternalNode() = false

    fun <T : Data> getConsumer(clazz: KClass<out Node>): T {
        val incomingNode = nodeDataContext.getIncomingNodes()[Utils.getNodeId(clazz)]
            ?: throw IllegalSelectionException("Node: ${Utils.getNodeId(clazz)} not found in incoming of ${Utils.getNodeId(this)}")
        return incomingNode.getData()
    }
}