package com.example.workflow.test

import com.example.workflow.engine.builder.DataFlowBuilder

class ExecutionTest  {

    // Data sets
    private val testData0: TestData0 = TestData0()
    private val testDataA: TestDataA = TestDataA()
    private val testDataB: TestDataB = TestDataB()
    private val testDataC: TestDataC = TestDataC()


    fun start() {

        val dataFlowManager = DataFlowBuilder()
            .register(NodeBuilderA(), testDataA, arrayOf(testData0))
            .register(NodeBuilderB(), testDataB, arrayOf(testDataA))
            .register(NodeBuilderC(), testDataC, arrayOf(testDataA, testDataB))
            .build()


        // execute data
        dataFlowManager.execute(testData0)
        dataFlowManager.execute(testDataA)
        dataFlowManager.execute(testDataB)

    }
}