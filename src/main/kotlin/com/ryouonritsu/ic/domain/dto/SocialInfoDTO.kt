package com.ryouonritsu.ic.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author ryouonritsu
 */
@Schema(description = "社会信息")
data class SocialInfoDTO(
    @Schema(description = "微信号", example = "wechat", required = false)
    var wechat: String = "",
    @Schema(description = "QQ号", example = "123456789", required = false)
    var qq: String = "",
    @Schema(description = "所在行业", example = "IT", required = false)
    var industry: String = "",
    @Schema(description = "公司", example = "Google", required = false)
    var company: String = "",
    @Schema(description = "职位", example = "Software Engineer", required = false)
    var position: String = "",
    @Schema(description = "职称", example = "Engineer", required = false)
    var jobTitle: String = "",
)
