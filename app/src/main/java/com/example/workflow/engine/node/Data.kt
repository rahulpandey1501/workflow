package com.example.workflow.engine.node

open abstract class Data {

    private lateinit var id: String

    fun getId(): String = id

    fun setId(id: String) {
        this.id = id
    }

    abstract fun reset()
}