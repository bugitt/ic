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
        val realName = row.getCellText(1)
        val phone = row.getCellText(2)
        val email = row.getCellText(3)
        // 如果phone和email都为空，则返回null
        if (phone == "" && email == "") return null
        val admissionYear = row.getCellText(5)
        val graduationYear = row.getCellText(6)
        // 学历
        val degree = row.getCellText(4)
        // 院系
        val school = row.getCellText(7)
        // 专业
        val major = row.getCellText(8)
        // 小班
        val grade = row.getCellText(9)
        // 大班
        val className = row.getCellText(10)
        // 国家
        val country = row.getCellText(11)
        // 省
        val province = row.getCellText(12)
        // 市
        val city = row.getCellText(13)
        // 行业
        val industry = row.getCellText(14)
        // 单位
        val company = row.getCellText(15)
        // 职务
        val job = row.getCellText(16)
        // 职称
        val position = row.getCellText(17)
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

fun Row.getCellText(index: Int) : String {
    return this.getCell(index)?.let {
        when (it.cellType) {
            CellType.NUMERIC -> it.numericCellValue.toLong().toString()
            CellType.STRING -> it.stringCellValue
            else -> ""
        }
    } ?: ""
}