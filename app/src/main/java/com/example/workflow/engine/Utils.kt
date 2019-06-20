package com.example.workflow.engine

import com.example.workflow.engine.annotations.NodeBuilderInfo
import com.example.workflow.engine.builder.NodeBuilder
import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

object Utils {

    fun getName(clazz: Class<*>): String = clazz.simpleName

    fun getOutgoingNodeId(nodeBuilder: NodeBuilder) : Array<KClass<out NodeBuilder>> {
        return findNodeBuilderAnnotations(nodeBuilder::class.java).outgoing
    }

    fun getConsumerDataId(nodeBuilder: NodeBuilder) : Array<KClass<out Data>> {
        return findNodeBuilderAnnotations(nodeBuilder::class.java).consumes
    }

    private fun findNodeBuilderAnnotations(clazz: Class<out NodeBuilder>): NodeBuilderInfo {
        return clazz.annotations.find { it.annotationClass == NodeBuilderInfo::class } as NodeBuilderInfo
    }
}