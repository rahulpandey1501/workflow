package com.example.workflow.engine.dataflow

import com.example.workflow.engine.Utils
import kotlin.reflect.KClass

open abstract class Data {

    abstract fun getId(): String

    abstract fun reset()
}

open abstract class DataAdapter(type: KClass<out Data>) : Data() {

    private var identifier: String = Utils.getName(type)

    override fun getId(): String = identifier
}

open class DataHolder(type: KClass<out Any>, var data: Data?) {

    private var identifier: String = Utils.getName(type.java)

    fun getId(): String = identifier

    fun resetData() {
        this.data = null
    }
}