package com.example.workflow.engine.node

import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

interface NodeContract {

    fun getNodeData(): Data
    fun getDataId(): String
    fun getNodeState(): NodeState
    fun getNodeMessage(): String?
    fun getNodeMeta(): NodeMeta
    fun addIncoming(node: Node?, dataClass: KClass<out Data>)
    fun addOutgoing(node: Node?, produce: KClass<out Data>)
    fun setNodeState(nodeState: NodeState)
    fun getIncomingNodes(): MutableCollection<Node>
    fun getOutgoingNodes(): MutableCollection<Node>
    fun setNodeStateMessage(message: String?)
    fun setNodeData(data: Data?)
}