package com.example.workflow.test

import com.example.workflow.engine.annotations.ExternalNodeInfo
import com.example.workflow.engine.annotations.NodeInfo
import com.example.workflow.engine.node.ExternalNode
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.node.NodeMeta
import com.example.workflow.engine.node.NodeState
import com.example.workflow.engine.nodeprocessorcontract.NodeProcessorCallback


@ExternalNodeInfo(data = TestData0::class)
class ExternalNode0 : ExternalNode() {

    override fun process(callback: NodeProcessorCallback) {

        val result = getResult<TestData0>()

        val nodeState = if (result.test2 == null || result.test1 == null) NodeState.INVALID else NodeState.VALID

        callback.updateNodeStatus(nodeState)
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {

    }
}

@NodeInfo(
    consumes = [ExternalNode0::class],
    produce = TestDataA::class
)
class NodeA : Node() {

    override fun process(callback: NodeProcessorCallback) {

        val incomingData = getData<TestData0>(ExternalNode0::class)

        if (null == incomingData.test1 || null == incomingData.test2) {
            callback.updateNodeStatus(NodeState.INVALID)

        } else {
            callback.updateNodeStatus(NodeState.WAITING, "Async call going for OfferApplicability")

            Thread(Runnable {
                val result = getResult<TestDataA>()
                Thread.sleep(500)
                result.test1 = "HelloFrom_A"
                result.test2 = "WorldFrom_A"
                callback.updateNodeStatus(NodeState.VALID)
            }).start()
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }
}

@NodeInfo(consumes = [NodeA::class], produce = TestDataB::class)
class NodeB : Node() {

    override fun process(callback: NodeProcessorCallback) {

        val incomingData = getData<TestDataA>(NodeA::class)

        if (null == incomingData.test1 || null == incomingData.test2) {
            callback.updateNodeStatus(NodeState.INVALID)

        } else {
            callback.updateNodeStatus(NodeState.WAITING)

            Thread(Runnable {
                Thread.sleep(500)
                val result = getResult<TestDataB>()
                result.test1 = "HelloFrom_B"
                result.test2 = "WorldFrom_B"
                callback.updateNodeStatus(NodeState.VALID)
            }).start()
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }

}

@NodeInfo(consumes = [NodeA::class, NodeB::class], produce = TestDataC::class)
class NodeC : Node() {

    override fun process(callback: NodeProcessorCallback) {

        val incomingDataA = getData<TestDataA>(NodeA::class)
        val incomingDataB = getData<TestDataB>(NodeB::class)

        if (null == incomingDataA.test1 || null == incomingDataA.test2 || null == incomingDataB.test1 || null == incomingDataB.test2) {
            callback.updateNodeStatus(NodeState.INVALID)

        } else {
            val result = getResult<TestDataC>()
            result.test1 = "HelloFrom_C"
            result.test2 = "WorldFrom_C"
            callback.updateNodeStatus(NodeState.VALID)
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }
}