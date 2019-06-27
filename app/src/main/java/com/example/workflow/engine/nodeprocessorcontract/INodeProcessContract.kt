package com.example.workflow.engine.nodeprocessorcontract

import com.example.workflow.engine.node.Data
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.node.NodeState
import kotlin.reflect.KClass

interface INodeProcessContract {

    fun updateNodeStatus(nodeState: NodeState, message: String? = null)
}