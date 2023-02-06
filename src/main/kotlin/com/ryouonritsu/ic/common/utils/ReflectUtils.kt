package com.ryouonritsu.ic.common.utils

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * @author ryouonritsu
 */
object ReflectUtils {
    fun <T : Any> copyPropertyNonNull(
        clz: KClass<T>,
        source: T,
        dest: T
    ) {
        clz.declaredMemberProperties.forEach {
            it.isAccessible = true
            val value = it.get(source)
            if (value is String && value.isNotBlank() || value !is String && value != null) {
                (it as KMutableProperty1<*, *>).setter.call(dest, value)
            }
        }
    }
}