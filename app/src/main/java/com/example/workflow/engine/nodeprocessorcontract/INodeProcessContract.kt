package com.example.workflow.engine.nodeprocessorcontract

import com.example.workflow.engine.node.Node
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.node.NodeState
import kotlin.reflect.KClass

interface INodeProcessContract {

    fun updateNodeStatus(node: Node, nodeState: NodeState, message: String? = null)
    fun <T : Data> getData(clazz: KClass<T>): T?
}