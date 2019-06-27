package com.example.workflow.engine.helper

import com.example.workflow.engine.Utils
import com.example.workflow.engine.node.Data
import com.example.workflow.engine.node.Node
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class DataManagerHelper {

    private val nodeMapping = hashMapOf<String, Node>()
    private val dataNodeMapping = hashMapOf<String, String>()

    fun getAllNodes() = nodeMapping.values

    fun populate(node: Node) {
        val nodeData = node.getNodeContract().getNodeData()
        nodeMapping[Utils.getNodeId(node)] = node
        dataNodeMapping[nodeData.getId()] = Utils.getNodeId(node)
    }

    fun getNode(data: Data): Node = nodeMapping[dataNodeMapping[data.getId()]]!!

    fun getNode(clazz: KClass<out Node>): Node = nodeMapping[Utils.getNodeId(clazz)]!!

    fun getTargetNode(): Node {
        return nodeMapping.values.first { it.isTargetNode() }
    }
}