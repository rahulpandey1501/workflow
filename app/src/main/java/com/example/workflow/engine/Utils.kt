package com.example.workflow.engine

import com.example.workflow.engine.annotations.ExternalNodeInfo
import com.example.workflow.engine.annotations.NodeInfo
import com.example.workflow.engine.node.Node
import com.example.workflow.engine.dataflow.Data
import kotlin.reflect.KClass

object Utils {

    fun getName(clazz: Class<*>): String = clazz.simpleName

    fun getName(clazz: KClass<out Data>): String = getName(clazz.java)

    fun getName(data: Data) = getName(data.javaClass)

    fun getName(node: Node): String = getName(node.javaClass)

    private fun getNodeInfoAnnotation(clazz: Class<out Node>): NodeInfo {
        return clazz.getAnnotation(NodeInfo::class.java)
    }

    private fun getExternalNodeInfoAnnotation(clazz: Class<out Node>): ExternalNodeInfo {
        return clazz.getAnnotation(ExternalNodeInfo::class.java)
    }

    fun getNodeConsumers(node: Node): Array<KClass<out Data>> {
        return if (node.isExternalNode()) {
            arrayOf()

        } else {
            getNodeInfoAnnotation(node.javaClass).consumes
        }
    }

    fun getNodeProduce(node: Node): KClass<out Data> {
        return if (node.isExternalNode()) {
            val externalNodeInfo = getExternalNodeInfoAnnotation(node.javaClass)
            externalNodeInfo.data

        } else {
            getNodeInfoAnnotation(node.javaClass).produce
        }
    }
}