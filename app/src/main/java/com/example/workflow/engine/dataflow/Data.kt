package com.example.workflow.engine.dataflow

import com.example.workflow.engine.Utils

open abstract class Data {

    abstract fun getId(): String
}

open abstract class DataAdapter<T : Data>(type: Class<T>) : Data() {
    private var identifier: String = Utils.getName(type)

    override fun getId(): String = identifier
}