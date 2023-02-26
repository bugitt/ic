package com.ryouonritsu.ic.entity

import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author ryouonritsu
 */
@Entity
class UserFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '文件ID'", nullable = false)
    var id: Long = 1L,
    @Column(columnDefinition = "TEXT COMMENT '文件URL'", nullable = false)
    var url: String,
    @Column(name = "file_path", columnDefinition = "TEXT COMMENT '文件路径'", nullable = false)
    var filePath: String = "",
    @Column(name = "file_name", columnDefinition = "TEXT COMMENT '文件名'", nullable = false)
    var fileName: String = "",
    @Column(name = "user_id", columnDefinition = "BIGINT DEFAULT '0' COMMENT '用户ID'", nullable = false)
    var userId: Long,
    @Column(name = "is_deleted", columnDefinition = "TINYINT(3) DEFAULT '0' COMMENT '是否已删除'", nullable = false)
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
}