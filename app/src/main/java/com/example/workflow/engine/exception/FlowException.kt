package com.example.workflow.engine.exception

import com.example.workflow.engine.Utils
import com.example.workflow.engine.builder.NodeBuilder


class FlowException : BaseException {
    constructor(message: String) : super(message)
    constructor(nodeBuilder: NodeBuilder) : this("Flow exception occurred on node ${Utils.getName(nodeBuilder.javaClass)}")
}