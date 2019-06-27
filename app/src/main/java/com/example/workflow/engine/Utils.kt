package com.example.workflow.engine

import com.example.workflow.engine.annotations.ExternalNodeInfo
import com.example.workflow.engine.annotations.NodeInfo
import com.example.workflow.engine.node.Data
import com.example.workflow.engine.node.Node
import kotlin.reflect.KClass

object Utils {

    private fun getClassName(clazz: Class<*>): String = clazz.simpleName

    fun getClassName(data: Data): String = getClassName(data.javaClass)

    fun getClassName(clazz: KClass<out Data>): String = getClassName(clazz.java)

    fun getNodeId(node: Node) = getClassName(node.javaClass)

    fun getNodeId(clazz: KClass<out Node>) = getClassName(clazz.java)

    private fun getNodeInfoAnnotation(clazz: Class<out Node>): NodeInfo {
        return clazz.getAnnotation(NodeInfo::class.java)
    }

    private fun getExternalNodeInfoAnnotation(clazz: Class<out Node>): ExternalNodeInfo {
        return clazz.getAnnotation(ExternalNodeInfo::class.java)
    }

    fun getNodeConsumers(node: Node): Array<KClass<out Node>> {
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