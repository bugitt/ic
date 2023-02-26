package com.ryouonritsu.ic.common.enums

import com.ryouonritsu.ic.common.exception.BaseExceptionStructure

/**
 * @author ryouonritsu
 */
enum class ExceptionEnum(
    override val code: String,
    override val message: String
) : BaseExceptionStructure {
    SUCCESS("200", "success"),
    BAD_REQUEST("400", "bad request, please check your request body or parameters"),
    UNAUTHORIZED("401", "unauthorized"),
    FORBIDDEN("403", "forbidden"),
    NOT_FOUND("404", "not found"),
    METHOD_NOT_ALLOWED("405", "method not allowed"),
    NOT_ACCEPTABLE("406", "not acceptable"),
    REQUEST_TIMEOUT("408", "request timeout"),
    INTERNAL_SERVER_ERROR("500", "internal server error"),
    SERVICE_UNAVAILABLE("503", "service unavailable"),
    UNEXPECTED_ERROR("10000", "An unexpected error has occurred!"),
    UNKNOWN_ERROR("10001", "unknown mistake"),
    OBJECT_DOES_NOT_EXIST("10002", "object does not exist in the database"),
    OBJECT_ALREADY_EXIST("10003", "object already exist in the database"),
    DATA_ERROR("10004", "data error"),
    PERMISSION_DENIED("10005", "permission denied"),
    TEMPLATE_NOT_EXIST("10006", "template does not exist"),
    COLUMN_NAME_IS_INVALID("10007", "column name is invalid"),
    FIELD_NOT_FOUND("10008", "field not found"),
    DATA_TYPE_IS_INVALID("10009", "data type is invalid");

    companion object {
        fun getByCode(code: String) = values().first { it.code == code }
    }
}