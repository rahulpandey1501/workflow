package com.example.workflow.engine.builder

import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.engine.node.*
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class NodeBuilder {

    lateinit var dataFlowManager: DataFlowManager
    private lateinit var nodeDataContext: NodeDataContext

    fun init(producer: Data, dataFlowManager: DataFlowManager) {
        val node = NodeMeta(producer)
        val nodeNavigator = NodeNavigator()
        this.nodeDataContext = NodeDataContext(node, nodeNavigator)
        this.dataFlowManager = dataFlowManager
    }

    fun getNodeContract() = nodeDataContext as NodeContract

    abstract fun process(resultInstance: Data)

    fun <T : Data> getIncomingData(clazz: KClass<T>): T {
//        return nodeDataContext.getIncomingNode(Utils.getName(clazz)) as T
        return dataFlowManager.dataNodeMappingHelper.getData(Utils.getName(clazz)) as T
    }

    fun <T : Data> getOutgoingData(clazz: KClass<T>): T {
        return dataFlowManager.dataNodeMappingHelper.getData(Utils.getName(clazz)) as T
    }

    fun getIncomingData(): MutableCollection<NodeBuilder> {
        return nodeDataContext.getIncomingNodes()
    }

    fun getOutgoingData(): MutableCollection<NodeBuilder> {
        return nodeDataContext.getOutgoingNodes()
    }

    fun updateNodeState(newNodeState: NodeState) {
        dataFlowManager.updateNodeState(newNodeState, this)
    }
}