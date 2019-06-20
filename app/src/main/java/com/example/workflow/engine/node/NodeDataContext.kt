package com.example.workflow.engine.node

import com.example.workflow.engine.builder.NodeBuilder

class NodeDataContext(
    private var nodeMeta: NodeMeta,
    private var nodeNavigation: NodeNavigator

) : NodeContract {

    override fun getNodeData() = nodeMeta.result

    override fun getDataId() = nodeMeta.result.getId()

    override fun getNodeState() = nodeMeta.state

    override fun getNodeMessage(): String? = nodeMeta.stateMessage

    override fun getNodeMeta(): NodeMeta = nodeMeta

    override fun addIncoming(nodeBuilder: NodeBuilder) {
        nodeNavigation.incoming[nodeBuilder.getNodeContract().getDataId()] = nodeBuilder
    }

    override fun getIncomingNodes() = nodeNavigation.incoming.values

    override fun getIncomingNode(dataId: String) = nodeNavigation.incoming[dataId]

    override fun getOutgoingNode(dataId: String) = nodeNavigation.outgoing[dataId]

    override fun addOutgoing(nodeBuilder: NodeBuilder) {
        nodeNavigation.outgoing[nodeBuilder.getNodeContract().getDataId()] = nodeBuilder
    }

    override fun getOutgoingNodes() = nodeNavigation.outgoing.values

    override fun setNodeState(nodeState: NodeState) {
        nodeMeta.state = nodeState
    }

    override fun setNodeStateMessage(message: String?) {
        nodeMeta.stateMessage = message
    }
}