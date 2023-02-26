package com.ryouonritsu.ic.component

import com.ryouonritsu.ic.common.enums.DataTypeEnum
import com.ryouonritsu.ic.common.enums.ExceptionEnum
import com.ryouonritsu.ic.common.exception.ServiceException
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.ReflectionUtils

val log: Logger = LoggerFactory.getLogger("TemplateProcessor")

fun <T> XSSFWorkbook.process(columnDefinitions: List<ColumnDSL>, data: List<T>): XSSFWorkbook {
    val sheet = this.createSheet()

    val headerRow = sheet.createRow(0)
    columnDefinitions.forEachIndexed { index, columnDSL ->
        val cell = headerRow.createCell(index)
        cell.cellStyle = this.createCellStyle().apply { setFont(this@process.createFont().apply { bold = true }) }
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
            when (DataTypeEnum.getByType(columnDSL.dataType ?: run {
                log.error("[TemplateProcessor::XSSFWorkbook.process] Data type is null")
                throw ServiceException(ExceptionEnum.DATA_TYPE_IS_INVALID)
            })) {
                DataTypeEnum.NUMBER -> row.createCell(id).setCellValue(value.toString().toDouble())
                DataTypeEnum.STRING -> row.createCell(id).setCellValue(value.toString())
            }
        }
    }

    headerRow.forEachIndexed { index, _ -> sheet.autoSizeColumn(index) }
    return this
}