package com.example.workflow.engine.node

import com.example.workflow.engine.Utils

class NodeDataContext(
    private var nodeMeta: NodeMeta,
    private var nodeNavigation: NodeNavigator

) : NodeContract {

    override fun getNodeData() = nodeMeta.result!!

    override fun getDataId() = getNodeData().getId()

    override fun getNodeState() = nodeMeta.state

    override fun getNodeMessage(): String? = nodeMeta.stateMessage

    override fun getNodeMeta(): NodeMeta = nodeMeta

    override fun addIncoming(node: Node?) {
        node?.let { nodeNavigation.incoming[Utils.getNodeId(node)] = node }
    }

    override fun addOutgoing(node: Node?) {
        node?.let { nodeNavigation.outgoing[Utils.getNodeId(node)] = node }
    }

    override fun getIncomingNodes() = nodeNavigation.incoming

    override fun getOutgoingNodes() = nodeNavigation.outgoing

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