package com.example.workflow

import com.example.workflow.engine.builder.DataFlowBuilder
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.test.NodeBuilderA
import com.example.workflow.test.NodeBuilderB
import com.example.workflow.test.NodeBuilderC
import com.example.workflow.test.TestData0
import com.example.workflow.test.TestDataA
import com.example.workflow.test.TestDataB
import com.example.workflow.test.TestDataC
import org.junit.Before
import org.junit.Test

class WorkFlowExecutionTest {

    lateinit var dataFlowManager: DataFlowManager

    // test data
    lateinit var testData0: TestData0
    lateinit var testDataA: TestDataA
    lateinit var testDataB: TestDataB
    lateinit var testDataC: TestDataC

    // nodes
    lateinit var nodeA: NodeBuilderA
    lateinit var nodeB: NodeBuilderB
    lateinit var nodeC: NodeBuilderC

    @Before
    fun setup() {
        testData0 = TestData0()
        testDataA = TestDataA()
        testDataB = TestDataB()
        testDataC = TestDataC()

        nodeA = NodeBuilderA()
        nodeB = NodeBuilderB()
        nodeC = NodeBuilderC()

        dataFlowManager = DataFlowBuilder()
            .register(nodeA, testDataA, arrayOf(testData0))
            .register(nodeB, testDataB, arrayOf(testDataA))
            .register(nodeC, testDataC, arrayOf(testDataA, testDataB))
            .build()

    }

    @Test
    fun testLoop() {


    }

}