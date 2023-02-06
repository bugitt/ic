package com.ryouonritsu.ic.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author ryouonritsu
 */
@Schema(description = "学校信息")
data class SchoolInfoDTO(
    @Schema(description = "学号", example = "19377054", required = false)
    var studentId: String = "",
    @Schema(description = "班级", example = "202115", required = false)
    var classId: String = "",
    @Schema(description = "入学年份", example = "2019", required = false)
    var admissionYear: String = "",
    @Schema(description = "毕业年份", example = "2023", required = false)
    var graduationYear: String = "",
    @Schema(description = "学院", example = "计算机学院", required = false)
    var college: String = "",
)
