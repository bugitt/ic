package com.ryouonritsu.ic.domain.protocol.request

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotNull

/**
 * @author ryouonritsu
 */
@Schema(description = "用户上传请求")
data class UserUploadRequest(
    @field:JsonIgnore
    @NotNull
    @Schema(description = "Excel文件", required = true)
    val file: MultipartFile?
)
