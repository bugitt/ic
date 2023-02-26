package com.ryouonritsu.ic.common.enums

/**
 * @author ryouonritsu
 */
enum class DataTypeEnum(
    val type: String
) {
    NUMBER("number"),
    STRING("string");

    companion object {
        fun getByType(type: String) = values().find { it.type == type } ?: STRING
    }
}