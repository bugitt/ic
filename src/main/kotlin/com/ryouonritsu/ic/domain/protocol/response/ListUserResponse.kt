package com.ryouonritsu.ic.domain.protocol.response

import com.ryouonritsu.ic.domain.dto.UserDTO
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author ryouonritsu
 */
@Schema(description = "用户列表查询响应")
data class ListUserResponse(
    @Schema(description = "总数", required = true)
    val total: Long,
    @Schema(description = "用户列表", required = true)
    val users: List<UserDTO>
)
