package com.ryouonritsu.ic.entity

import javax.persistence.*

/**
 * @author ryouonritsu
 */
@Entity
class UserFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '文件ID'")
    var id: Long = 0L,
    @Column(columnDefinition = "TEXT COMMENT '文件URL'")
    var url: String,
    @Column(name = "file_path", columnDefinition = "TEXT COMMENT '文件路径'")
    var filePath: String = "",
    @Column(name = "file_name", columnDefinition = "TEXT COMMENT '文件名'")
    var fileName: String = "",
    @Column(name = "user_id", columnDefinition = "BIGINT COMMENT '用户ID'")
    var userId: Long,
) {
}