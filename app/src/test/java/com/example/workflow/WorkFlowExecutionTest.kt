package com.example.workflow

import com.example.workflow.engine.builder.DataFlowBuilder
import com.example.workflow.engine.dataflow.DataFlowManager
import com.example.workflow.test.NodeA
import com.example.workflow.test.NodeB
import com.example.workflow.test.NodeC
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
            .register(NodeA(), testDataA)
            .register(NodeB(), testDataB)
            .register(NodeC(), testDataC)
            .build()

    }

    @Test
    fun testLoop() {
        dataFlowManager.execute(testData0)

        Thread {

            Thread.sleep(3000)
            testDataA.test1 = null
            dataFlowManager.execute(testDataA)

            Thread.sleep(1000)
            testDataB.test1 = null
            dataFlowManager.execute(testDataA)

            Thread.sleep(1000)
            testDataA.test1 = "Test1"
            testDataB.test1 = "Test2"
            dataFlowManager.execute(testDataA)

        }.start()

    }

}