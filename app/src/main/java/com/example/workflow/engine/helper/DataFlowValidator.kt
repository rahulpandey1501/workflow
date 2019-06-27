package com.example.workflow.engine.helper

import com.example.workflow.engine.Utils
import com.example.workflow.engine.node.Data
import com.example.workflow.engine.exception.BaseException
import com.example.workflow.engine.exception.FlowException
import com.example.workflow.engine.exception.LoopDetectionException
import com.example.workflow.engine.exception.TypeMismatchException
import com.example.workflow.engine.node.Node
import kotlin.reflect.KClass

class DataFlowValidator {

    @Throws(BaseException::class)
    fun validate(nodes: Collection<Node>) {
        checkForLoopAndProducer(nodes)
        checkForOrphanNodes(nodes)
        checkForTargetNode(nodes)
    }

    private fun checkForOrphanNodes(nodes: Collection<Node>) {
        val orphanNode =
            nodes.firstOrNull { it.getIncomingNodes().isEmpty() && it.getOutgoingNodes().isEmpty() }
        orphanNode?.let { throw FlowException("Workflow orphan node found Node: ${Utils.getNodeId(orphanNode::class)}") }
    }

    /* Optional check, just to restrict to one final output */
    private fun checkForTargetNode(nodes: Collection<Node>) {
        val targetNodes = nodes.filter { it.isTargetNode() }
        val targetNodeCount = targetNodes.size

        if (targetNodeCount == 0) {
            throw FlowException("No target node found")

        } else if (targetNodeCount > 1) {
            val targetNodeIds = targetNodes.map { Utils.getNodeId(it) }
            throw FlowException("More than one target node found $targetNodeIds")
        }
    }

    private fun checkForLoopAndProducer(nodes: Collection<Node>) {
        nodes.forEach { nodeBuilder ->
            nodeBuilder.getOutgoingNodes().forEach {
                if (it.getOutgoingNodes().contains(nodeBuilder)) {
                    throw LoopDetectionException(it, nodeBuilder)
                }
                nodeDataSanity(it, Utils.getNodeProduce(it))
            }
        }
    }

    private fun nodeDataSanity(node: Node, produce: KClass<out Data>) {
        val nodeData = node.getNodeContract().getNodeData()
        if (Utils.getClassName(nodeData) != Utils.getClassName(produce)) {
            throw TypeMismatchException("${Utils.getNodeId(node)} cannot produce ${Utils.getClassName(produce)} it should be instance of ${Utils.getClassName(nodeData)}")
        }
    }
}