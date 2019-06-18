package com.example.workflow.engine.node

import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data

interface NodeContract {

    fun getNodeData(): Data
    fun getDataId(): String
    fun getNodeState(): NodeState
    fun addIncoming(nodeBuilder: NodeBuilder)
    fun addOutgoing(nodeBuilder: NodeBuilder)
    fun setNodeState(nodeState: NodeState)
    fun getIncomingNodes(): MutableCollection<NodeBuilder>
    fun getOutgoingNodes(): MutableCollection<NodeBuilder>
    fun getIncomingNode(dataId: String): NodeBuilder?
    fun getOutgoingNode(dataId: String): NodeBuilder?
}