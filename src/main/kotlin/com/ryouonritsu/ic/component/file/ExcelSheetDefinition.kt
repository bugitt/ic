package com.ryouonritsu.ic.component.file

import com.ryouonritsu.ic.component.ColumnDSL

/**
 * @author ryouonritsu
 */
data class ExcelSheetDefinition(
    var sheetIndex: Int,
    var sheetName: String,
    var columns: List<ColumnDSL>
)
