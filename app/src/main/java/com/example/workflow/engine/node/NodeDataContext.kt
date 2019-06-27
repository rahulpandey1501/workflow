package com.example.workflow.engine.node

import com.example.workflow.engine.Utils
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

    override fun addIncoming(node: Node?, dataClass: KClass<out Data>) {
        node?.let { nodeNavigation.incoming[Utils.getName(dataClass)] = node }
    }

    override fun addOutgoing(node: Node?, produce: KClass<out Data>) {
        node?.let { nodeNavigation.outgoing[Utils.getName(produce)] = node }
    }

    override fun getIncomingNodes() = nodeNavigation.incoming.values

    override fun getOutgoingNodes() = nodeNavigation.outgoing.values

    override fun setNodeState(nodeState: NodeState) {
        nodeMeta.state = nodeState
    }

    override fun setNodeStateMessage(message: String?) {
        nodeMeta.stateMessage = message
    }

    override fun setNodeData(data: Data?) {
        nodeMeta.result = data
    }
}