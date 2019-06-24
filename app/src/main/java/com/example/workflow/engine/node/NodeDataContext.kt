package com.example.workflow.engine.node

import com.example.workflow.engine.Utils
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

class NodeDataContext(
    private var nodeMeta: NodeMeta,
    private var nodeNavigation: NodeNavigator

) : NodeContract {

    override fun getNodeData() = nodeMeta.result!!

    override fun getDataId() = getNodeData().getId()

    override fun getNodeState() = nodeMeta.state

    override fun getNodeMessage(): String? = nodeMeta.stateMessage

    override fun getNodeMeta(): NodeMeta = nodeMeta

    override fun addIncoming(nodeBuilder: NodeBuilder?, dataClass: KClass<out Data>) {
        nodeBuilder?.let { nodeNavigation.incoming[Utils.getName(dataClass)] = nodeBuilder }
    }

    override fun addOutgoing(nodeBuilder: NodeBuilder?, produce: KClass<out Data>) {
        nodeBuilder?.let { nodeNavigation.outgoing[Utils.getName(produce)] = nodeBuilder }
    }

    override fun getIncomingNodes() = nodeNavigation.incoming.values

    override fun getOutgoingNodes() = nodeNavigation.outgoing.values

    override fun setNodeState(nodeState: NodeState) {
        nodeMeta.state = nodeState
    }

    override fun setNodeStateMessage(message: String?) {
        nodeMeta.stateMessage = message
    }

    override fun setNodeData(data: Data) {
        nodeMeta.result = data
    }
}