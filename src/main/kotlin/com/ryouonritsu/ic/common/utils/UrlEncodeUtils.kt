package com.ryouonritsu.ic.common.utils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * @author ryouonritsu
 */
object UrlEncodeUtils {
    fun encode(value: String) =
        runCatching { URLEncoder.encode(value, StandardCharsets.UTF_8.name()) }.getOrNull()
}