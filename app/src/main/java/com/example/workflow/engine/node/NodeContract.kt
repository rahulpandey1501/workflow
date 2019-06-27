package com.example.workflow.engine.node

interface NodeContract {

    fun getNodeData(): Data
    fun getDataId(): String
    fun getNodeState(): NodeState
    fun getNodeMessage(): String?
    fun getNodeMeta(): NodeMeta
    fun addIncoming(node: Node?)
    fun addOutgoing(node: Node?)
    fun setNodeState(nodeState: NodeState)
    fun getIncomingNodes(): Map<String, Node>
    fun getOutgoingNodes(): Map<String, Node>
    fun setNodeStateMessage(message: String?)
    fun setNodeData(data: Data?)
}