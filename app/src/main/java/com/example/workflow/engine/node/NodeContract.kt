package com.example.workflow.engine.node

import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

interface NodeContract {

    fun getNodeData(): Data
    fun getDataId(): String
    fun getNodeState(): NodeState
    fun getNodeMessage(): String?
    fun getNodeMeta(): NodeMeta
    fun addIncoming(nodeBuilder: NodeBuilder?, dataClass: KClass<out Data>)
    fun addOutgoing(nodeBuilder: NodeBuilder?, produce: KClass<out Data>)
    fun setNodeState(nodeState: NodeState)
    fun getIncomingNodes(): MutableCollection<NodeBuilder>
    fun getOutgoingNodes(): MutableCollection<NodeBuilder>
    fun setNodeStateMessage(message: String?)
    fun setNodeData(data: Data)
}