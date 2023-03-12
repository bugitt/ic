package com.ryouonritsu.ic.service

import com.ryouonritsu.ic.component.ColumnDSL
import com.ryouonritsu.ic.domain.dto.UserDTO
import com.ryouonritsu.ic.domain.protocol.request.ModifyUserInfoRequest
import com.ryouonritsu.ic.domain.protocol.response.ListUserResponse
import com.ryouonritsu.ic.domain.protocol.response.Response
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.web.multipart.MultipartFile

/**
 * @author ryouonritsu
 */
interface UserService {
    fun sendRegistrationVerificationCode(email: String?, modify: Boolean): Response<Unit>
    fun register(
        email: String?,
        verificationCode: String?,
        username: String?,
        password1: String?,
        password2: String?,
        avatar: String,
        realName: String,
    ): Response<Unit>

    fun login(
        username: String?,
        password: String?,
        keepLogin: Boolean
    ): Response<List<Map<String, String>>>

    fun showInfo(userId: Long): Response<List<UserDTO>>
    fun selectUserByUserId(userId: Long): Response<List<UserDTO>>
    fun sendForgotPasswordEmail(email: String?): Response<Unit>
    fun changePassword(
        mode: Int?,
        oldPassword: String?,
        password1: String?,
        password2: String?,
        email: String?,
        verifyCode: String?
    ): Response<Unit>

    fun uploadFile(file: MultipartFile): Response<List<Map<String, String>>>
    fun deleteFile(url: String): Response<Unit>
    fun modifyUserInfo(request: ModifyUserInfoRequest): Response<Unit>
    fun modifyEmail(
        email: String?,
        verifyCode: String?,
        password: String?
    ): Response<Unit>

    fun queryHeaders(): Response<List<ColumnDSL>>
    fun list(
        realName: String?,
        gender: String?,
        birthday: String?,
        location: String?,
        studentId: String?,
        classId: String?,
        admissionYear: String?,
        graduationYear: String?,
        college: String?,
        industry: String?,
        company: String?,
        page: Int,
        limit: Int
    ): Response<ListUserResponse>

    fun download(): XSSFWorkbook
    fun downloadTemplate(): XSSFWorkbook
    fun upload(file: MultipartFile): Response<Unit>
}