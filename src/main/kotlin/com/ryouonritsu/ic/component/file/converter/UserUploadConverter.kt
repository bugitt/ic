package com.ryouonritsu.ic.component.file.converter

import com.alibaba.fastjson2.toJSONString
import com.ryouonritsu.ic.common.utils.MD5Util
import com.ryouonritsu.ic.component.ColumnDSL
import com.ryouonritsu.ic.domain.dto.SchoolInfoDTO
import com.ryouonritsu.ic.domain.dto.SocialInfoDTO
import com.ryouonritsu.ic.domain.dto.UserInfoDTO
import com.ryouonritsu.ic.entity.User
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import java.time.LocalDate

/**
 * @author ryouonritsu
 */
object UserUploadConverter {
    fun convert(row: Row, columnDefinitions: List<ColumnDSL>): User {
        val realName = row.getCell(0).stringCellValue
        val email = row.getCell(1).stringCellValue
        val gender = row.getCell(2).stringCellValue
        val phone = when (row.getCell(3).cellType) {
            CellType.NUMERIC -> row.getCell(3).numericCellValue.toLong().toString()
            CellType.STRING -> row.getCell(3).stringCellValue
            else -> ""
        }
        val company = row.getCell(4).stringCellValue
        val industry = row.getCell(5).stringCellValue
        val position = row.getCell(6).stringCellValue
        val birthday = LocalDate.parse(row.getCell(7).stringCellValue)
        val location = row.getCell(8).stringCellValue
        val studentId = when (row.getCell(9).cellType) {
            CellType.NUMERIC -> row.getCell(9).numericCellValue.toLong().toString()
            CellType.STRING -> row.getCell(9).stringCellValue
            else -> ""
        }
        val admissionYear = row.getCell(10).numericCellValue.toLong().toString()
        val graduationYear = row.getCell(11).numericCellValue.toLong().toString()
        val description = row.getCell(12).stringCellValue
        val password = MD5Util.encode(studentId)
        val user = User(
            username = studentId,
            password = password,
            email = email,
            realName = realName,
            gender = User.Gender.getByDesc(gender).code,
            phone = phone,
            birthday = birthday,
            location = location,
            description = description
        )
        val schoolInfo = SchoolInfoDTO(
            studentId = studentId,
            admissionYear = admissionYear,
            graduationYear = graduationYear
        )
        val socialInfo = SocialInfoDTO(
            company = company,
            industry = industry,
            position = position
        )
        val userInfo = UserInfoDTO(schoolInfo, socialInfo)
        user.userInfo = userInfo.toJSONString()
        return user
    }
}