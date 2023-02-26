package com.ryouonritsu.ic.component

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import com.alibaba.fastjson2.annotation.JSONField
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author ryouonritsu
 */
@Schema(description = "列特定领域语言定义")
data class ColumnDSL(
    @Schema(description = "列ID", example = "1", required = true)
    var id: Int?,
    @Schema(description = "列名", example = "name", required = true)
    var columnName: String?,
    @Schema(description = "列标题", example = "姓名", required = true)
    var defaultTitle: String?,
    @Schema(description = "数据类型", example = "string", required = true)
    var dataType: String?,
    @Schema(description = "附加信息")
    var extra: JSONObject?,
    @Schema(description = "对应key", example = "[\"name\"]", required = true)
    @JSONField(serializeFeatures = [JSONWriter.Feature.WriteNullListAsEmpty])
    var keys: List<String>
)
