package com.example.workflow.engine

import com.example.workflow.engine.annotations.NodeBuilderInfo
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

object Utils {

    fun getName(clazz: Class<*>): String = clazz.simpleName

    fun getName(clazz: KClass<out Data>): String = getName(clazz.java)

    fun getName(data: Data) = getName(data.javaClass)

    fun getName(nodeBuilder: NodeBuilder): String = getName(nodeBuilder.javaClass)

    fun getBuilderAnnotation(nodeBuilder: NodeBuilder): NodeBuilderInfo {
        return getBuilderAnnotation(nodeBuilder.javaClass)
    }

    fun getBuilderAnnotation(clazz: Class<out NodeBuilder>): NodeBuilderInfo {
        return clazz.getAnnotation(NodeBuilderInfo::class.java)
    }
}