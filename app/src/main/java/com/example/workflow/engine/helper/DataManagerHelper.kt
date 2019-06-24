package com.example.workflow.engine.helper

import com.example.workflow.engine.Utils
import com.example.workflow.engine.annotations.NodeBuilderInfo
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.exception.TypeMismatchException
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class DataManagerHelper {

    private val nodeMapping = hashMapOf<String, NodeBuilder>()
    private val dataNodeMapping = hashMapOf<String, Pair<Data, NodeBuilder?>>()
    private val dataToNodeDependencyMapping = hashMapOf<String, Set<NodeBuilder>>()

    fun getNodeMapping() = nodeMapping

    fun populate(nodeBuilder: NodeBuilder) {
        val builderAnnotation = Utils.getBuilderAnnotation(nodeBuilder.javaClass)
        nodeDataSanity(nodeBuilder, builderAnnotation)
        val nodeData = nodeBuilder.getNodeContract().getNodeData()
        dataNodeMapping[Utils.getName(nodeData.javaClass)] = Pair(nodeData, nodeBuilder)
        nodeMapping[Utils.getName(nodeBuilder.javaClass)] = nodeBuilder
        builderAnnotation.consumes.forEach {
            val mapping = dataToNodeDependencyMapping.getOrPut(Utils.getName(it), { mutableSetOf(nodeBuilder) })
            dataToNodeDependencyMapping[Utils.getName(it)] = mapping.plus(nodeBuilder)
        }
    }

    private fun nodeDataSanity(nodeBuilder: NodeBuilder, nodeBuilderInfo: NodeBuilderInfo) {
        val nodeData = nodeBuilder.getNodeContract().getNodeData()
        if (Utils.getName(nodeData) != Utils.getName(nodeBuilderInfo.produce)) {
            throw TypeMismatchException("${Utils.getName(nodeBuilder)} cannot produce ${Utils.getName(nodeData)} it should be instance of ${nodeBuilderInfo.produce.java}")
        }
    }

    fun <T : Data> getNodeData(clazz: KClass<T>): T? {
        return getNodeData(Utils.getName(clazz.java)) as T
    }

    fun <T : Data> getNode(clazz: KClass<T>): NodeBuilder? = dataNodeMapping[Utils.getName(clazz)]?.second

    private fun getNodeData(dataId: String): Data? = dataNodeMapping[dataId]?.first

    fun addNodeData(data: Data) {
        val mapping = dataNodeMapping[Utils.getName(data)]
        dataNodeMapping[Utils.getName(data)] = Pair(data, mapping?.second)
    }

    fun getOutgoingNodes(data: Data) = dataToNodeDependencyMapping[Utils.getName(data)]

    fun getResultNode(): NodeBuilder {
        return nodeMapping.values.first { it.getOutgoingNode().isNullOrEmpty() }
    }
}