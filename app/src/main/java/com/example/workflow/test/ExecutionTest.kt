package com.example.workflow.test

import android.util.Log
import com.example.workflow.engine.builder.DataFlowBuilder

class ExecutionTest {

    fun start() {

        val testDataA = TestDataA()
        val testDataB = TestDataB()
        val testDataC = TestDataC()
        val testData0 = TestData0()

        val dataFlowManager = DataFlowBuilder()
            .register(ExternalNode0(), testData0)
            .register(NodeA(), testDataA)
            .register(NodeB(), testDataB)
            .register(NodeC(), testDataC, true)
            .build()

        // execute data
        dataFlowManager.execute(testData0)

        Thread {

//            Thread.sleep(3000)
//            testDataA.test1 = null
//            dataFlowManager.execute(testDataA)
//
            Thread.sleep(3000)
            testDataA.test1 = "INVALID"
            dataFlowManager.traceWorkFlowStatus()
            Log.d("workflow", "Testcase 2 for invalid")
            dataFlowManager.execute(testDataA)

//            Thread.sleep(1000)
//            testDataA.test1 = "Test1"
//            testDataB.test1 = "Test2"
//            dataFlowManager.execute(testDataA)

        }.start()


        Thread {
            Thread.sleep(4000)
            dataFlowManager.traceWorkFlowStatus()
        }.start()
    };
}