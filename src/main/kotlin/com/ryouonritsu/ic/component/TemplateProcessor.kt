package com.ryouonritsu.ic.component

import com.ryouonritsu.ic.common.enums.DataTypeEnum
import com.ryouonritsu.ic.common.enums.ExceptionEnum
import com.ryouonritsu.ic.common.exception.ServiceException
import com.ryouonritsu.ic.component.file.ExcelSheetDefinition
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.ReflectionUtils
import org.springframework.web.multipart.MultipartFile

val log: Logger = LoggerFactory.getLogger("TemplateProcessor")

/**
 * @author ryouonritsu
 */
fun <T> XSSFWorkbook.process(
    columnDefinitions: List<ColumnDSL>,
    data: List<T>,
    sheetName: String? = null
): XSSFWorkbook {
    val sheet = if (sheetName != null) this.createSheet(sheetName) else this.createSheet()

    val headerRow = sheet.createRow(0)
    columnDefinitions.forEachIndexed { index, columnDSL ->
        val cell = headerRow.createCell(index)
        cell.cellStyle = this.createCellStyle()
            .apply {
                setFont(this@process.createFont().apply { bold = true })
                borderTop = BorderStyle.THIN
                borderBottom = BorderStyle.THIN
                borderLeft = BorderStyle.THIN
                borderRight = BorderStyle.THIN
            }
        cell.setCellValue(columnDSL.defaultTitle)
    }

    data.forEachIndexed { index, t ->
        val row = sheet.createRow(index + 1)
        columnDefinitions.forEachIndexed { id, columnDSL ->
            val field = ReflectionUtils.findField(t!!::class.java, columnDSL.columnName ?: run {
                log.error("[TemplateProcessor::XSSFWorkbook.process] Column name is null")
                throw ServiceException(ExceptionEnum.COLUMN_NAME_IS_INVALID)
            })
            if (field == null) {
                log.error("[TemplateProcessor::XSSFWorkbook.process] Field not found: ${columnDSL.columnName}")
                throw ServiceException(ExceptionEnum.FIELD_NOT_FOUND)
            }
            ReflectionUtils.makeAccessible(field)
            val value = ReflectionUtils.getField(field, t)
            val cell = row.createCell(id)
            when (DataTypeEnum.getByType(columnDSL.dataType ?: run {
                log.error("[TemplateProcessor::XSSFWorkbook.process] Data type is null")
                throw ServiceException(ExceptionEnum.DATA_TYPE_IS_INVALID)
            })) {
                DataTypeEnum.NUMBER -> cell.setCellValue(value.toString().toDouble())
                DataTypeEnum.STRING -> cell.setCellValue(value.toString())
            }
            cell.cellStyle = this.createCellStyle()
                .apply {
                    borderTop = BorderStyle.THIN
                    borderBottom = BorderStyle.THIN
                    borderLeft = BorderStyle.THIN
                    borderRight = BorderStyle.THIN
                }
        }
        row.forEachIndexed { id, _ -> sheet.autoSizeColumn(id) }
    }

    return this
}

/**
 * @author ryouonritsu
 */
fun <T> MultipartFile.read(
    excelSheetDefinitions: List<ExcelSheetDefinition>,
    converter: (row: Row, columnDefinitions: List<ColumnDSL>) -> T
): List<T> {
    val workbook = XSSFWorkbook(this.inputStream)
    val data = mutableListOf<T>()
    workbook.forEachIndexed { index, sheet ->
        val columnDefinitions = excelSheetDefinitions[index].columns
        sheet.forEach {
            if (it.rowNum == 0) return@forEach
            converter(it, columnDefinitions)?.let { data += it }
        }
    }
    return data
}

/**
 * @author ryouonritsu
 */
fun <T> XSSFWorkbook.getTemplate(
    excelSheetDefinitions: List<ExcelSheetDefinition>,
    examples: List<T> = listOf()
): XSSFWorkbook {
    excelSheetDefinitions.forEach {
        this.process(it.columns, examples, it.sheetName)
    }
    return this
}