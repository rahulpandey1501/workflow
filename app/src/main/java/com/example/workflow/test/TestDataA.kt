package com.example.workflow.test

import com.example.workflow.engine.dataflow.DataAdapter

class TestData0 : DataAdapter<TestData0>(TestData0::class.java) {

    var test1: String? = "Hello"
    var test2: String? = "World"

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataA : DataAdapter<TestDataA>(TestDataA::class.java) {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataB : DataAdapter<TestDataB>(TestDataB::class.java) {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataC : DataAdapter<TestDataC>(TestDataC::class.java) {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}


