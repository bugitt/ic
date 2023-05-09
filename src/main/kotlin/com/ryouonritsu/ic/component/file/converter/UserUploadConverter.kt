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
    fun convert(row: Row, columnDefinitions: List<ColumnDSL>): User? {
        val realName = row.getCell(1)?.stringCellValue ?: ""
        val phone = row.getCell(2)?.let {
            when (it.cellType) {
                CellType.NUMERIC -> it.numericCellValue.toLong().toString()
                CellType.STRING -> it.stringCellValue
                else -> ""
            }
        } ?: ""
        val email = row.getCell(3)?.stringCellValue ?: ""
        // 如果phone和email都为空，则返回null
        if (phone == "" && email == "") return null
        val admissionYear = row.getCell(5)?.let {
            when (it.cellType) {
                CellType.NUMERIC -> it.numericCellValue.toLong().toString()
                CellType.STRING -> it.stringCellValue
                else -> ""
            }
        } ?: ""
        val graduationYear = row.getCell(6)?.let {
            when (it.cellType) {
                CellType.NUMERIC -> it.numericCellValue.toLong().toString()
                CellType.STRING -> it.stringCellValue
                else -> ""
            }
        } ?: ""
        // 学历
        val degree = row.getCell(4)?.stringCellValue ?: ""
        // 院系
        val school = row.getCell(7)?.stringCellValue ?: ""
        // 专业
        val major = row.getCell(8)?.stringCellValue ?: ""
        // 小班
        val grade = row.getCell(9)?.stringCellValue ?: ""
        // 大班
        val className = row.getCell(10)?.stringCellValue ?: ""
        // 国家
        val country = row.getCell(11)?.stringCellValue ?: ""
        // 省
        val province = row.getCell(12)?.stringCellValue ?: ""
        // 市
        val city = row.getCell(13)?.stringCellValue ?: ""
        // 行业
        val industry = row.getCell(14)?.stringCellValue ?: ""
        // 单位
        val company = row.getCell(15)?.stringCellValue ?: ""
        // 职务
        val job = row.getCell(16)?.stringCellValue ?: ""
        // 职称
        val position = row.getCell(17)?.stringCellValue ?: ""
        val birthday = LocalDate.parse("1970-01-01")
        val location = country + province + city
        val description = school + major + grade + className + degree
        val password = MD5Util.encode("@buaa2023")
        val user = User(
            username = phone.ifEmpty { email },
            password = password,
            email = email,
            realName = realName,
            gender = 0,
            phone = phone,
            birthday = birthday,
            location = location,
            description = description
        )
        val schoolInfo = SchoolInfoDTO(
            studentId = "",
            admissionYear = admissionYear,
            graduationYear = graduationYear
        )
        val socialInfo = SocialInfoDTO(
            company = company,
            industry = industry,
            position = "$job，$position"
        )
        val userInfo = UserInfoDTO(schoolInfo, socialInfo)
        user.userInfo = userInfo.toJSONString()
        return user
    }
}