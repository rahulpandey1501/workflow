package com.example.workflow.engine.node

enum class NodeState { IDLE, VALID, INVALID, WAITING }

class NodeMeta(var result: Data? = null) {
    var state: NodeState = NodeState.IDLE
    var stateMessage: String? = null
}

class NodeNavigator {
    var incoming: HashMap<String, Node> = hashMapOf()
    var outgoing: HashMap<String, Node> = hashMapOf()
}