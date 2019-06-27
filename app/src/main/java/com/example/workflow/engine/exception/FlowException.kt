package com.example.workflow.engine.exception

import com.example.workflow.engine.Utils
import com.example.workflow.engine.node.Node


class FlowException : BaseException {
    constructor(message: String) : super(message)
    constructor(node: Node) : this("Flow exception occurred on node ${Utils.getName(node.javaClass)}")
}