package com.example.workflow.test

import com.example.workflow.engine.annotations.ExternalNodeInfo
import com.example.workflow.engine.annotations.NodeInfo
import com.example.workflow.engine.node.ExternalNode
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.node.NodeMeta
import com.example.workflow.engine.node.NodeState


@ExternalNodeInfo(data = TestData0::class)
class ExternalNode0 : ExternalNode() {

    override fun process(callback: (NodeState, String?) -> Unit) {

        val result = getData<TestData0>()

        val nodeState = if (result.test2 == null || result.test1 == null) NodeState.INVALID else NodeState.VALID

        callback(nodeState, "Executed Node 0")
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {

    }
}

@NodeInfo(
    consumes = [ExternalNode0::class],
    produce = TestDataA::class
)
class NodeA : Node() {

    override fun process(callback: (NodeState, String?) -> Unit) {

        val incomingData = getConsumer<TestData0>(ExternalNode0::class)

        if (null == incomingData.test1 || null == incomingData.test2) {
            callback(NodeState.INVALID, "Executed Node A")

        } else {
            callback(NodeState.WAITING, "Async call going for OfferApplicability")

            Thread(Runnable {
                val result = getData<TestDataA>()
                Thread.sleep(500)
                result.test1 = "HelloFrom_A"
                result.test2 = "WorldFrom_A"
                callback(NodeState.VALID, "Executed Node A")
            }).start()
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }
}

@NodeInfo(consumes = [NodeA::class], produce = TestDataB::class)
class NodeB : Node() {

    override fun process(callback: (NodeState, String?) -> Unit) {

        val incomingData = getConsumer<TestDataA>(NodeA::class)

        if (null == incomingData.test1 || null == incomingData.test2) {
            callback(NodeState.INVALID, "Executed Node B")

        } else {
            callback(NodeState.WAITING, "Executed Node B")

            Thread(Runnable {
                Thread.sleep(500)
                val result = getData<TestDataB>()
                result.test2 = "WorldFrom_B"
                result.test1 = "WorldFrom_A"
                callback(NodeState.VALID, "Executed Node B")

            }).start()
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }

}

@NodeInfo(consumes = [NodeA::class, NodeB::class], produce = TestDataC::class)
class NodeC : Node() {

    override fun process(callback: (NodeState, String?) -> Unit) {

        val incomingDataA = getConsumer<TestDataA>(NodeA::class)
        val incomingDataB = getConsumer<TestDataB>(NodeB::class)

        if (null == incomingDataA.test1 || null == incomingDataA.test2 || null == incomingDataB.test1 || null == incomingDataB.test2) {
            callback(NodeState.INVALID, "Executed Node 0")

        } else {
            val result = getData<TestDataC>()
            result.test1 = "HelloFrom_C"
            result.test2 = "WorldFrom_C"
            callback(NodeState.VALID, "Executed Node C")
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }
}