package com.example.workflow.engine.exception

import com.example.workflow.engine.Utils
import com.example.workflow.engine.builder.NodeBuilder

class LoopDetectionException(firstNode: NodeBuilder, secondNode: NodeBuilder) :
    Exception("Loop detected between ${Utils.getName(firstNode.javaClass)} and ${Utils.getName(secondNode.javaClass)}")

class FlowException : Exception {
    constructor(message: String) : super(message)
    constructor(nodeBuilder: NodeBuilder) : this("Flow exception occurred on node ${Utils.getName(nodeBuilder.javaClass)}")
}