package com.ryouonritsu.ic.entity

import com.ryouonritsu.ic.domain.dto.UserDTO
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author ryouonritsu
 */
@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var email: String,
    var username: String,
    var password: String,
    var credit: Long = 0,
    var avatar: String = "",
    @Column(name = "registration_time")
    var registrationTime: LocalDateTime = LocalDateTime.now(),
    @Column(name = "real_name")
    var realName: String = "",
    @Column(name = "is_certified", columnDefinition = "TINYINT(3) DEFAULT 0", nullable = false)
    var isCertified: Boolean = false,
    @Column(name = "educational_background")
    var educationalBackground: String = "",
    @Column(name = "is_admin", columnDefinition = "TINYINT(3) DEFAULT 0", nullable = false)
    var isAdmin: Boolean = false,
) {
    fun toDTO() = UserDTO(
        id = "$id",
        email = email,
        username = username,
        password = password,
        credit = "$credit",
        avatar = avatar,
        registrationTime = registrationTime,
        realName = realName,
        isCertified = isCertified,
        educationalBackground = educationalBackground,
        isAdmin = isAdmin,
    )
}