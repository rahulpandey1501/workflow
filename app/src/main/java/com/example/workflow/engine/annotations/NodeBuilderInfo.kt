package com.example.workflow.engine.annotations

import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class NodeBuilderInfo(
    val consumes: Array<KClass<out Data>>,
    val optional: Array<KClass<out Data>>,
    val produce: KClass<out Data>
)