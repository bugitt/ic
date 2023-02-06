package com.ryouonritsu.ic.common.utils

import org.springframework.util.DigestUtils

/**
 * @author ryouonritsu
 */
object MD5Util {
    private const val SALT = "ryouonritsu_ic"

    fun encode(string: String?) = DigestUtils.md5DigestAsHex(((string ?: "") + SALT).toByteArray())
}