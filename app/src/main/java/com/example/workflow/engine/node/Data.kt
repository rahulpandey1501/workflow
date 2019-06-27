package com.example.workflow.engine.node

import com.example.workflow.engine.Utils

open abstract class Data {

    open fun getId(): String = Utils.getClassName(this)

    abstract fun reset()
}