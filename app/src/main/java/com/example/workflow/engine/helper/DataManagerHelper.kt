package com.example.workflow.engine.helper

import com.example.workflow.engine.Utils
import com.example.workflow.engine.annotations.NodeInfo
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.exception.TypeMismatchException
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class DataManagerHelper {

    private val nodeMapping = hashMapOf<String, Node>()
    private val dataNodeMapping = hashMapOf<String, Pair<Data, Node>>()
    private val dataToNodeDependencyMapping = hashMapOf<String, Set<Node>>()

    fun getNodeMapping() = nodeMapping

    fun populate(node: Node) {
        val produce = Utils.getNodeProduce(node)
        val consumers = Utils.getNodeConsumers(node)
        nodeDataSanity(node, produce)
        val nodeData = node.getNodeContract().getNodeData()
        dataNodeMapping[Utils.getName(nodeData.javaClass)] = Pair(nodeData, node)
        nodeMapping[Utils.getName(node.javaClass)] = node
        consumers.forEach {
            val mapping = dataToNodeDependencyMapping.getOrPut(Utils.getName(it), { mutableSetOf(node) })
            dataToNodeDependencyMapping[Utils.getName(it)] = mapping.plus(node)
        }
    }

    private fun nodeDataSanity(node: Node, produce: KClass<out Data>) {
        val nodeData = node.getNodeContract().getNodeData()
        if (Utils.getName(nodeData) != Utils.getName(produce)) {
            throw TypeMismatchException("${Utils.getName(node)} cannot produce ${Utils.getName(nodeData)} it should be instance of ${produce.java}")
        }
    }

    fun <T : Data> getNodeData(clazz: KClass<T>): T? {
        return getNodeData(Utils.getName(clazz.java)) as T
    }

    fun getNode(clazz: Class<out Data>): Node = dataNodeMapping[Utils.getName(clazz)]!!.second

    private fun getNodeData(dataId: String): Data? = dataNodeMapping[dataId]?.first

    fun addNodeData(data: Data) {
        val mapping = dataNodeMapping[Utils.getName(data)]!!
        dataNodeMapping[Utils.getName(data)] = Pair(data, mapping.second)
    }

    fun getOutgoingNodes(data: Data) = dataToNodeDependencyMapping[Utils.getName(data)]

    fun getResultNode(): Node {
        return nodeMapping.values.first { it.getOutgoingNode().isNullOrEmpty() }
    }
}