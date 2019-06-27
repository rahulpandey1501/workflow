package com.example.workflow.engine.node

/**
 * This node is used for the direct input to graph, which has no dependencies (example user input)
 */
abstract class ExternalNode: Node() {

    override fun isExternalNode(): Boolean = true
}