package com.ryouonritsu.ic.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author ryouonritsu
 */
@Schema(description = "用户信息")
data class UserInfoDTO(
    @Schema(description = "学校信息", required = true)
    var schoolInfo: SchoolInfoDTO,
    @Schema(description = "社会信息", required = true)
    var socialInfo: SocialInfoDTO,
)
