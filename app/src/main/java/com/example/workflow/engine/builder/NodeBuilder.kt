package com.example.workflow.engine.builder

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.engine.node.*
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class NodeBuilder {

    lateinit var dataFlowManager: DataFlowManager
    private lateinit var nodeDataContext: NodeDataContext

    abstract fun process(resultInstance: Data)
    abstract fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta)

    fun init(producer: Data, dataFlowManager: DataFlowManager) {
        val node = NodeMeta(producer)
        val nodeNavigator = NodeNavigator()
        this.nodeDataContext = NodeDataContext(node, nodeNavigator)
        this.dataFlowManager = dataFlowManager
    }

    fun getNodeContract() = nodeDataContext as NodeContract

    fun <T : Data> getIncomingData(clazz: KClass<T>): T {
        return dataFlowManager.dataNodeMappingHelper.getData(Utils.getName(clazz.java)) as T
    }

    fun <T : Data> getOutgoingNodes(clazz: KClass<T>): T {
        return dataFlowManager.dataNodeMappingHelper.getData(Utils.getName(clazz.java)) as T
    }

    fun getIncomingData(): MutableCollection<NodeBuilder> {
        return nodeDataContext.getIncomingNodes()
    }

    fun getOutgoingNodes(): MutableCollection<NodeBuilder> {
        return nodeDataContext.getOutgoingNodes()
    }

    fun isResultNode(): Boolean = nodeDataContext.getOutgoingNodes().isEmpty()

    fun updateNodeState(newNodeState: NodeState, message: String? = null) {

        log(newNodeState)

        nodeDataContext.setNodeStateMessage(message)
        onStatusUpdated(newNodeState, getNodeContract().getNodeMeta())

        dataFlowManager.updateNodeState(newNodeState, this)

    }

    private fun log(newNodeState: NodeState) {
        Log.d("workflow", "Process: ${Utils.getName(javaClass)} | previous: ${getNodeContract().getNodeState()}  Current: ${newNodeState}")
    }
}