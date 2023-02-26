package com.ryouonritsu.ic.common.utils

/**
 * @author ryouonritsu
 */
object HttpHeaderValueUtils {
    fun getContentDisposition(fileName: String, inline: Boolean): String {
        return "${if (inline) "inline" else "attachment"};filename=${UrlEncodeUtils.encode(fileName)}"
    }
}