package com.ryouonritsu.ic.common.utils

/**
 * @author ryouonritsu
 */
object RequestContext {
    var userId = ThreadLocal<Long?>()
}