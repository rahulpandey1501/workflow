package com.example.workflow.engine.node

import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.builder.NodeBuilder

enum class NodeState { IDLE, VALID, INVALID, WAITING }

class NodeMeta(
    var result: Data,
    var state: NodeState = NodeState.IDLE,
    var stateMessage: String? = null
)

class NodeNavigator {
    var incoming: MutableMap<String, NodeBuilder> = hashMapOf()
    var outgoing: MutableMap<String, NodeBuilder> = hashMapOf()
}