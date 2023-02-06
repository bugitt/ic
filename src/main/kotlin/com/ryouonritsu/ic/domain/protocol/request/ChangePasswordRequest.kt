package com.ryouonritsu.ic.domain.protocol.request

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author ryouonritsu
 */
@Schema(description = "修改用户密码请求")
data class ChangePasswordRequest(
    @Schema(description = "旧密码")
    val oldPassword: String?,
    @Schema(description = "新密码", required = true)
    val password1: String?,
    @Schema(description = "确认新密码", required = true)
    val password2: String?,
    @Schema(description = "邮箱")
    val email: String?,
    @Schema(description = "验证码")
    val verifyCode: String?
)
