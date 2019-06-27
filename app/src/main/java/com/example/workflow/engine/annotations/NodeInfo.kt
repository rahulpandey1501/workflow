package com.example.workflow.engine.annotations

import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class NodeInfo(
    val consumes: Array<KClass<out Data>>,
    val optional: Array<KClass<out Data>>,
    val produce: KClass<out Data>
)