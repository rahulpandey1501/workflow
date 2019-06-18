package com.example.workflow.test

import android.util.Log
import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.node.NodeState
import com.example.workflow.engine.annotations.NodeBuilderClassInfo

@NodeBuilderClassInfo(consumes = [TestData0::class], produce = TestDataA::class, outgoing = [NodeBuilderB::class, NodeBuilderC::class])
class NodeBuilderA : NodeBuilder() {

    override fun process(resultInstance: Data) {
        val result = resultInstance as TestDataA
        result.test1 = "HelloFrom_A"
        result.test2 = "WorldFrom_A"

        val incomingData = getIncomingData(TestData0::class)

        Log.d("workflow", "Process: NodeBuilderA ${getNodeContract().getNodeState()}  ${NodeState.WAITING}")

        updateNodeState(NodeState.WAITING)
    }
}

@NodeBuilderClassInfo(consumes = [TestDataA::class], produce = TestDataB::class, outgoing = [NodeBuilderC::class])
class NodeBuilderB : NodeBuilder() {

    override fun process(resultInstance: Data) {
        val result = resultInstance as TestDataB
        result.test1 = "HelloFrom_B"
        result.test2 = "WorldFrom_B"

        Log.d("workflow", "Process: NodeBuilderB ${getNodeContract().getNodeState()}  ${NodeState.INVALID}")

        updateNodeState(NodeState.INVALID)
    }

}

@NodeBuilderClassInfo(consumes = [TestDataA::class, TestDataB::class], produce = TestDataC::class, outgoing = [])
class NodeBuilderC : NodeBuilder() {

    override fun process(resultInstance: Data) {
        val result = resultInstance as TestDataC
        result.test1 = "HelloFrom_C"
        result.test2 = "WorldFrom_C"

        Log.d("workflow", "Process: NodeBuilderC ${getNodeContract().getNodeState()}  ${NodeState.VALID}")

        updateNodeState(NodeState.VALID)
    }
}