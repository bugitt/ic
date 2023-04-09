package com.ryouonritsu.ic.repository

import com.ryouonritsu.ic.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation
import org.springframework.stereotype.Repository

/**
 * @author ryouonritsu
 */
@Repository
interface UserRepository : JpaRepositoryImplementation<User, Long> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?

    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND (u.id = ?1 OR u.username LIKE %?1% OR u.realName LIKE %?1%) LIMIT 10")
    fun findByKeyword(keyword: String): List<User>

    @Query("SELECT u FROM User u WHERE u.isDeleted = false ORDER BY u.createTime")
    fun list(pageable: Pageable = PageRequest.of(0, 10)): Page<User>

    @Query("SELECT u FROM User u WHERE u.username LIKE %?1% OR u.realName LIKE %?1%")
    fun findByUsernameOrRealNameLike(
        username: String,
        pageable: Pageable = PageRequest.of(0, 1)
    ): Page<User>
}