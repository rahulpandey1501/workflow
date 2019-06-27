package com.example.workflow.engine.node

import com.example.workflow.engine.dataflow.Data

enum class NodeState { IDLE, VALID, INVALID, WAITING }

class NodeMeta(var result: Data? = null) {
    var state: NodeState = NodeState.IDLE
    var stateMessage: String? = null
}

class NodeNavigator {
    var incoming: MutableMap<String, Node> = hashMapOf()
    var outgoing: MutableMap<String, Node> = hashMapOf()
}