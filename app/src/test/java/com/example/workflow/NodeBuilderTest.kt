package com.example.workflow

import android.util.Log
import com.example.workflow.engine.Utils
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.node.NodeState
import com.example.workflow.engine.annotations.NodeBuilderInfo
import com.example.workflow.engine.node.NodeMeta

@NodeBuilderInfo(
    consumes = [TestData0::class],
    produce = TestDataA::class,
    outgoing = [NodeBuilderB::class, NodeBuilderC::class],
    optional = []
)
class NodeBuilderA : NodeBuilder() {

    override fun process(resultInstance: Data) {

        val incomingData = getIncomingData(TestData0::class)

        if (null == incomingData.test1 || null == incomingData.test2) {
            updateNodeState(NodeState.INVALID)

        } else {
            updateNodeState(NodeState.WAITING, "Async call going for OfferApplicability")

            Thread(Runnable {
                val result = resultInstance as TestDataA
                Thread.sleep(500)
                result.test1 = "HelloFrom_A"
                result.test2 = "WorldFrom_A"
                updateNodeState(NodeState.VALID)
            }).start()
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {
        Log.d("workflow", "StatusUpdated: ${Utils.getName(javaClass)}  $nodeState")
    }
}

@NodeBuilderInfo(consumes = [TestDataA::class], produce = TestDataB::class, outgoing = [NodeBuilderC::class])
class NodeBuilderB : NodeBuilder() {

    override fun process(resultInstance: Data) {


        val incomingData = getIncomingData(TestDataA::class)

        if (null == incomingData.test1 || null == incomingData.test2) {
            updateNodeState(NodeState.INVALID)

        } else {
            updateNodeState(NodeState.WAITING)

            Thread(Runnable {
                Thread.sleep(500)
                val result = resultInstance as TestDataB
                result.test1 = "HelloFrom_B"
                result.test2 = "WorldFrom_B"
                updateNodeState(NodeState.VALID)
            }).start()
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {

        Log.d("workflow", "StatusUpdated: ${Utils.getName(javaClass)}  $nodeState")
    }

}

@NodeBuilderInfo(consumes = [TestDataA::class, TestDataB::class], produce = TestDataC::class, outgoing = [])
class NodeBuilderC : NodeBuilder() {

    override fun process(resultInstance: Data) {


        val incomingDataA = getIncomingData(TestDataA::class)
        val incomingDataB = getIncomingData(TestDataB::class)

        if (null == incomingDataA.test1 || null == incomingDataA.test2 || null == incomingDataB.test1 || null == incomingDataB.test2) {
            updateNodeState(NodeState.INVALID)

        } else {
            val result = resultInstance as TestDataC
            result.test1 = "HelloFrom_C"
            result.test2 = "WorldFrom_C"
            updateNodeState(NodeState.VALID)
        }
    }

    override fun onStatusUpdated(nodeState: NodeState, nodeMeta: NodeMeta) {

        Log.d("workflow", "StatusUpdated: ${Utils.getName(javaClass)}  $nodeState")
    }
}