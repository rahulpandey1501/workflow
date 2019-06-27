package com.example.workflow.engine.exception

import com.example.workflow.engine.Utils
import com.example.workflow.engine.node.Node


class LoopDetectionException : BaseException {

    constructor(firstNode: Node, secondNode: Node) :
            super("Loop detected between ${Utils.getName(firstNode.javaClass)} and ${Utils.getName(secondNode.javaClass)}")
}