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

    @Before
    fun setup() {
        testData0 = TestData0()
        testDataA = TestDataA()
        testDataB = TestDataB()
        testDataC = TestDataC()

        dataFlowManager = DataFlowBuilder()
            .register(NodeBuilderA(), testDataA)
            .register(NodeBuilderB(), testDataB)
            .register(NodeBuilderC(), testDataC)
            .build()

    }

    @Test
    fun testLoop() {


    }

}