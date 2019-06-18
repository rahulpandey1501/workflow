package com.example.workflow.engine

import com.example.workflow.engine.annotations.NodeBuilderClassInfo
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

object Utils {

    fun getName(clazz: Class<*>): String = clazz.simpleName

    fun getName(clazz: KClass<*>): String = getName(clazz.java)

    fun getOutgoingNodeId(nodeBuilder: NodeBuilder) : Array<KClass<out NodeBuilder>> {
        return findDataBuilderClassInfoAnnotations(nodeBuilder::class.java).outgoing
    }

    fun getConsumerDataId(nodeBuilder: NodeBuilder) : Array<KClass<out Data>> {
        return findDataBuilderClassInfoAnnotations(nodeBuilder::class.java).consumes
    }

    fun findDataBuilderClassInfoAnnotations(clazz: Class<out NodeBuilder>): NodeBuilderClassInfo {
        return clazz.annotations.find { it.annotationClass == NodeBuilderClassInfo::class } as NodeBuilderClassInfo
    }
}