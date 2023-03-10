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
    @Column(columnDefinition = "BIGINT COMMENT '用户ID'", nullable = false)
    var id: Long = 0,
    @Column(columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '邮箱'", nullable = false)
    var email: String,
    @Column(columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '用户名'", nullable = false)
    var username: String,
    @Column(columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '密码'", nullable = false)
    var password: String,
    @Column(columnDefinition = "TEXT COMMENT '头像地址'")
    var avatar: String = "",
    @Column(
        name = "real_name",
        columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '真实姓名'",
        nullable = false
    )
    var realName: String = "",
    @Column(
        name = "gender",
        columnDefinition = "TINYINT(3) DEFAULT '0' COMMENT '性别, 0保密, 1男, 2女'",
        nullable = false
    )
    var gender: Int = 0,
    @Column(
        name = "birthday",
        columnDefinition = "DATE DEFAULT '1900-01-01' COMMENT '生日'",
        nullable = false
    )
    var birthday: LocalDate = LocalDate.of(1900, 1, 1),
    @Column(
        name = "phone",
        columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '联系方式'",
        nullable = false
    )
    var phone: String = "",
    @Column(
        name = "location",
        columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '所在地'",
        nullable = false
    )
    var location: String = "",
    @Column(
        name = "educational_background",
        columnDefinition = "TEXT COMMENT '教育背景'"
    )
    var educationalBackground: String = "",
    @Column(name = "description", columnDefinition = "TEXT COMMENT '个人简介'")
    var description: String = "",
    @Column(name = "user_info", columnDefinition = "LONGTEXT COMMENT '用户信息JSON'")
    var userInfo: String = UserInfoDTO(SchoolInfoDTO(), SocialInfoDTO()).toJSONString(),
    @Column(
        name = "is_admin",
        columnDefinition = "TINYINT(3) DEFAULT '0' COMMENT '是否为管理员'",
        nullable = false
    )
    var isAdmin: Boolean = false,
    @Column(
        name = "is_deleted",
        columnDefinition = "TINYINT(3) DEFAULT '0' COMMENT '是否已删除'",
        nullable = false
    )
    var isDeleted: Boolean = false,
    @Column(
        name = "create_time",
        columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'",
        nullable = false
    )
    var createTime: LocalDateTime = LocalDateTime.now(),
    @Column(
        name = "modify_time",
        columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'",
        nullable = false
    )
    var modifyTime: LocalDateTime = LocalDateTime.now(),
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
            fun getByDesc(desc: String) = values().find { it.desc == desc } ?: SECRET
        }
    }

    fun toDTO(): UserDTO {
        val userInfo = this.userInfo.to<UserInfoDTO>()
        return UserDTO(
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
            description = description,
            company = userInfo.socialInfo.company ?: "",
            industry = userInfo.socialInfo.industry ?: "",
            position = userInfo.socialInfo.position ?: "",
            studentId = userInfo.schoolInfo.studentId ?: "",
            admissionYear = userInfo.schoolInfo.admissionYear ?: "",
            graduationYear = userInfo.schoolInfo.graduationYear ?: "",
            userInfo = userInfo,
            isAdmin = isAdmin,
            isDeleted = isDeleted,
            registrationTime = createTime
        )
    }
}