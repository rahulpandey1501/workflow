package com.example.workflow.test

import com.example.workflow.engine.builder.DataFlowBuilder

class ExecutionTest  {

    // Data sets
    private val testData0: TestData0 = TestData0()
    private val testDataA: TestDataA = TestDataA()
    private val testDataB: TestDataB = TestDataB()
    private val testDataC: TestDataC = TestDataC()

    private val nodeA = NodeBuilderA()
    private val nodeB = NodeBuilderB()
    private val nodeC = NodeBuilderC()

    fun start() {

        val dataFlowManager = DataFlowBuilder()
            .register(nodeA, testDataA, arrayOf(testData0))
            .register(nodeB, testDataB, arrayOf(testDataA))
            .register(nodeC, testDataC, arrayOf(testDataA, testDataB))
            .build()

        // execute data
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

//        dataFlowManager.traceWorkFlowStatus()
//        dataFlowManager.traceWorkFlowStatus(nodeB)

    }
}