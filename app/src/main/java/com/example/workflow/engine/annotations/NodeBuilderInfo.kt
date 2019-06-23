package com.example.workflow.engine.annotations

import com.example.workflow.engine.dataflow.Data
import com.example.workflow.engine.builder.NodeBuilder
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class NodeBuilderInfo(
    val consumes: Array<KClass<out Data>>,
    val optional: Array<KClass<out Data>>,
    val produce: KClass<out Data>,
    val outgoing: Array<KClass<out NodeBuilder>>
)