package com.ryouonritsu.ic.repository

import com.ryouonritsu.ic.entity.TableTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * @author ryouonritsu
 */
interface TableTemplateRepository : JpaRepository<TableTemplate, Long> {
    @Query("SELECT t FROM TableTemplate t WHERE t.templateType = ?1 AND t.isDeleted = false ORDER BY t.createTime DESC")
    fun findByTemplateType(templateType: Int): List<TableTemplate>
}