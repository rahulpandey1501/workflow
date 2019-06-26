package com.example.workflow.test

import com.example.workflow.engine.annotations.NodeBuilderInfo
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.node.NodeMeta
import com.example.workflow.engine.node.NodeState
import com.example.workflow.engine.nodeprocessorcontract.NodeProcessorCallback

@NodeBuilderInfo(
    consumes = [TestData0::class],
    produce = TestDataA::class,
    optional = []
)
class NodeBuilderA : NodeBuilder() {

    override fun process(callback: NodeProcessorCallback) {
        val incomingData = callback.getData(TestData0::class)

        if (null == incomingData?.test1 || null == incomingData.test2) {
            callback.updateNodeStatus(this, NodeState.INVALID)

        } else {
            callback.updateNodeStatus(this, NodeState.WAITING, "Async call going for OfferApplicability")

            Thread(Runnable {
                val result = getResult<TestDataA>()
                Thread.sleep(500)
                result.test1 = "HelloFrom_A"
                result.test2 = "WorldFrom_A"
                callback.updateNodeStatus(this, NodeState.VALID)
            }).start()
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }
}

@NodeBuilderInfo(consumes = [TestDataA::class], produce = TestDataB::class, optional = [])
class NodeBuilderB : NodeBuilder() {

    override fun process(callback: NodeProcessorCallback) {

        val incomingData = callback.getData(TestDataA::class)

        if (null == incomingData?.test1 || null == incomingData.test2) {
            callback.updateNodeStatus(this, NodeState.INVALID)

        } else {
            callback.updateNodeStatus(this, NodeState.WAITING)

            Thread(Runnable {
                Thread.sleep(500)
                val result = getResult<TestDataB>()
                result.test1 = "HelloFrom_B"
                result.test2 = "WorldFrom_B"
                callback.updateNodeStatus(this, NodeState.VALID)
            }).start()
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }

}

@NodeBuilderInfo(consumes = [TestDataA::class, TestDataB::class], produce = TestDataC::class, optional = [])
class NodeBuilderC : NodeBuilder() {

    override fun process(callback: NodeProcessorCallback) {

        val incomingDataA = callback.getData(TestDataA::class)
        val incomingDataB = callback.getData(TestDataB::class)

        if (null == incomingDataA?.test1 || null == incomingDataA.test2 || null == incomingDataB?.test1 || null == incomingDataB.test2) {
            callback.updateNodeStatus(this, NodeState.INVALID)

        } else {
            val result = getResult<TestDataC>()
            result.test1 = "HelloFrom_C"
            result.test2 = "WorldFrom_C"
            callback.updateNodeStatus(this, NodeState.VALID)
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
    }
}