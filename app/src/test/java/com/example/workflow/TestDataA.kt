package com.example.workflow

import com.example.workflow.engine.dataflow.DataAdapter

class TestData0 : DataAdapter(TestData0::class) {

    var test1: String? = "Hello"
    var test2: String? = "World"

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataA : DataAdapter(TestDataA::class) {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataB : DataAdapter(TestDataB::class) {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataC : DataAdapter(TestDataC::class) {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}


