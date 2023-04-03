package com.ryouonritsu.ic.domain.protocol.request

import com.ryouonritsu.ic.domain.dto.UserInfoDTO
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author ryouonritsu
 */
@Schema(description = "修改用户信息请求")
data class ModifyUserInfoRequest(
    @Schema(description = "用户Id，管理员可用")
    var id: Long?,
    @Schema(description = "邮箱，管理员可用")
    var email: String?,
    @Schema(description = "用户名")
    val username: String?,
    @Schema(description = "个人头像")
    val avatar: String?,
    @Schema(description = "真实姓名")
    val realName: String?,
    @Schema(description = "性别", example = "保密/男/女")
    val gender: String?,
    @Schema(description = "生日", example = "2000-01-01")
    val birthday: String?,
    @Schema(description = "联系方式", example = "12345678901")
    val phone: String?,
    @Schema(description = "所在地", example = "China")
    val location: String?,
    @Schema(description = "学历")
    val educationalBackground: String?,
    @Schema(description = "简介和主要成就")
    val description: String?,
    @Schema(description = "用户信息")
    val userInfo: UserInfoDTO?,
    @Schema(description = "是否为管理员")
    val isAdmin: Boolean?,
    @Schema(description = "是否已删除，管理员可用")
    var isDeleted: Boolean?,
)
