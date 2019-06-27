package com.example.workflow.engine.annotations

import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class ExternalNodeInfo(
    val data: KClass<out Data>
)