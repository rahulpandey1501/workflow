package com.example.workflow.test

import com.example.workflow.engine.node.Data

class TestData0 : Data() {

    var test1: String? = "Hello"
    var test2: String? = "World"

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataA : Data() {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataB : Data() {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}

class TestDataC : Data() {

    var test1: String? = null
    var test2: String? = null

    override fun reset() {
        test1 = null; test2 = null
    }
}


