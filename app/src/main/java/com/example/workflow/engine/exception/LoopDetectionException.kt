package com.example.workflow.engine.exception

import com.example.workflow.engine.Utils
import com.example.workflow.engine.builder.NodeBuilder


class LoopDetectionException : BaseException {

    constructor(firstNode: NodeBuilder, secondNode: NodeBuilder) :
            super("Loop detected between ${Utils.getName(firstNode.javaClass)} and ${Utils.getName(secondNode.javaClass)}")
}