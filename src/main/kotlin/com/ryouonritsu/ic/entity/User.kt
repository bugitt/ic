package com.ryouonritsu.ic.entity

import com.alibaba.fastjson2.to
import com.alibaba.fastjson2.toJSONString
import com.ryouonritsu.ic.domain.dto.SchoolInfoDTO
import com.ryouonritsu.ic.domain.dto.SocialInfoDTO
import com.ryouonritsu.ic.domain.dto.UserDTO
import com.ryouonritsu.ic.domain.dto.UserInfoDTO
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author ryouonritsu
 */
@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '用户ID'")
    var id: Long = 0,
    @Column(columnDefinition = "VARCHAR(255) COMMENT '邮箱'")
    var email: String,
    @Column(columnDefinition = "VARCHAR(255) COMMENT '用户名'")
    var username: String,
    @Column(columnDefinition = "VARCHAR(255) COMMENT '密码'")
    var password: String,
    @Column(columnDefinition = "VARCHAR(255) COMMENT '头像地址'")
    var avatar: String = "",
    @Column(name = "real_name", columnDefinition = "VARCHAR(255) COMMENT '真实姓名'")
    var realName: String = "",
    @Column(name = "gender", columnDefinition = "TINYINT(3) DEFAULT 0 COMMENT '性别, 0保密, 1男, 2女'")
    var gender: Int = 0,
    @Column(name = "birthday", columnDefinition = "DATE COMMENT '生日'")
    var birthday: LocalDate? = null,
    @Column(name = "phone", columnDefinition = "VARCHAR(255) COMMENT '联系方式'")
    var phone: String = "",
    @Column(name = "location", columnDefinition = "VARCHAR(255) COMMENT '所在地'")
    var location: String = "",
    @Column(name = "educational_background", columnDefinition = "VARCHAR(255) COMMENT '教育背景'")
    var educationalBackground: String = "",
    @Column(name = "user_info", columnDefinition = "LONGTEXT COMMENT '用户信息JSON'")
    var userInfo: String = UserInfoDTO(SchoolInfoDTO(), SocialInfoDTO()).toJSONString(),
    @Column(name = "is_admin", columnDefinition = "TINYINT(3) DEFAULT 0 COMMENT '是否为管理员'", nullable = false)
    var isAdmin: Boolean = false,
    @Column(name = "is_deleted", columnDefinition = "TINYINT(3) DEFAULT 0 COMMENT '是否已删除'", nullable = false)
    var isDeleted: Boolean = false,
    @Column(name = "registration_time", columnDefinition = "DATETIME COMMENT '注册时间'")
    var registrationTime: LocalDateTime = LocalDateTime.now(),
) {
    enum class Gender(
        val code: Int,
        val desc: String
    ) {
        SECRET(0, "保密"),
        MALE(1, "男"),
        FEMALE(2, "女");

        companion object {
            fun valueOf(code: Int) = values().find { it.code == code } ?: SECRET
        }
    }

    fun toDTO() = UserDTO(
        id = "$id",
        email = email,
        username = username,
        avatar = avatar,
        realName = realName,
        gender = Gender.valueOf(gender).desc,
        birthday = birthday,
        phone = phone,
        location = location,
        educationalBackground = educationalBackground,
        userInfo = userInfo.to<UserInfoDTO>(),
        isAdmin = isAdmin,
        isDeleted = isDeleted,
        registrationTime = registrationTime
    )
}