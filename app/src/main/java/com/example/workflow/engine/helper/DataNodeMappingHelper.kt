package com.example.workflow.engine.helper

import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data

class DataNodeMappingHelper {

    // data to NodeBuilder dependencies mapping
    var dataNodesMapping: MutableMap<String, Pair<Data, Set<NodeBuilder>>> = hashMapOf()

    private fun has(dataId: String): Boolean = dataNodesMapping.containsKey(dataId)

    fun getData(dataId: String) = dataNodesMapping[dataId]?.first!!

    fun getOutgoingNodes(dataId: String) = dataNodesMapping[dataId]?.second

    fun getOutgoingNodes(data: Data) = dataNodesMapping[data.getId()]?.second

    fun getOutgoingNodes(nodeBuilder: NodeBuilder) = dataNodesMapping[nodeBuilder.getNodeContract().getDataId()]?.second

    fun put(data: Data, nodeBuilder: NodeBuilder) {
        val dataId = data.getId()

        if (has(dataId)) {
            val builderSet = dataNodesMapping[dataId]!!
            dataNodesMapping[dataId] = Pair(data, builderSet.second.plus(nodeBuilder))

        } else {
            dataNodesMapping[dataId] = Pair(data, setOf(nodeBuilder))

        }
    }

}