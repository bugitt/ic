package com.ryouonritsu.ic.controller

import com.ryouonritsu.ic.common.annotation.AuthCheck
import com.ryouonritsu.ic.common.annotation.ServiceLog
import com.ryouonritsu.ic.common.enums.AuthEnum
import com.ryouonritsu.ic.common.utils.DownloadUtils
import com.ryouonritsu.ic.common.utils.RedisUtils
import com.ryouonritsu.ic.common.utils.RequestContext
import com.ryouonritsu.ic.component.log
import com.ryouonritsu.ic.domain.protocol.request.*
import com.ryouonritsu.ic.domain.protocol.response.Response
import com.ryouonritsu.ic.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * @author ryouonritsu
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口")
class UserController(
    private val userService: UserService,
    private val redisUtils: RedisUtils
) {
    @ServiceLog(description = "发送注册验证码")
    @PostMapping("/sendRegistrationVerificationCode")
    @Tag(name = "用户接口")
    @Operation(
        summary = "发送注册验证码",
        description = "发送注册验证码到指定邮箱, 若modify为true, 则发送修改邮箱验证码, 默认为false"
    )
    fun sendRegistrationVerificationCode(@RequestBody request: SendRegistrationVerificationCodeRequest) =
        userService.sendRegistrationVerificationCode(request.email, request.modify)

    @ServiceLog(description = "用户注册")
    @PostMapping("/register")
    @Tag(name = "用户接口")
    @Operation(summary = "用户注册", description = "除了真实姓名其余必填")
    fun register(@RequestBody request: RegisterRequest) = userService.register(
        request.email,
        request.verificationCode,
        request.username,
        request.password1,
        request.password2,
        request.avatar,
        request.realName
    )

    @ServiceLog(description = "用户登录")
    @PostMapping("/login")
    @Tag(name = "用户接口")
    @Operation(
        summary = "用户登录",
        description = "keep_login为true时, 保持登录状态, 否则token会在3天后失效, 默认为false"
    )
    fun login(@RequestBody request: LoginRequest) =
        userService.login(request.username, request.password, request.keepLogin)

    @ServiceLog(description = "用户登出")
    @GetMapping("/logout")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(summary = "用户登出")
    fun logout(): Response<Any> {
        redisUtils - "${RequestContext.userId.get()}"
        return Response.success("登出成功")
    }

    @ServiceLog(description = "返回已登陆用户的信息")
    @GetMapping("/showInfo")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(summary = "返回已登陆用户的信息", description = "需要用户登陆才能查询成功")
    fun showInfo() = userService.showInfo(RequestContext.userId.get()!!)

    @ServiceLog(description = "根据用户id查询用户信息")
    @GetMapping("/selectUserByUserId")
    @Tag(name = "用户接口")
    @Operation(summary = "根据用户id查询用户信息")
    fun selectUserByUserId(
        @RequestParam("user_id") @Parameter(
            description = "用户id",
            required = true
        ) userId: Long
    ) = userService.selectUserByUserId(userId)

    @ServiceLog(description = "发送找回密码验证码")
    @PostMapping("/sendForgotPasswordEmail")
    @Tag(name = "用户接口")
    @Operation(summary = "发送找回密码验证码", description = "发送找回密码验证码到指定邮箱")
    fun sendForgotPasswordEmail(@RequestBody request: SendForgotPasswordEmailRequest) =
        userService.sendForgotPasswordEmail(request.email)

    @ServiceLog(description = "通过邮箱修改用户密码")
    @PostMapping("/changePasswordByEmail")
    @Tag(name = "用户接口")
    @Operation(
        summary = "通过邮箱修改用户密码",
        description = "需要提供邮箱, 验证码, 新密码和确认密码"
    )
    fun changePasswordByEmail(@RequestBody request: ChangePasswordRequest) =
        userService.changePassword(
            0,
            null,
            request.password1,
            request.password2,
            request.email,
            request.verifyCode
        )

    @ServiceLog(description = "通过原密码修改用户密码")
    @PostMapping("/changePasswordByOldPassword")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(
        summary = "通过原密码修改用户密码",
        description = "需要提供原密码, 新密码和确认密码"
    )
    fun changePasswordByOldPassword(@RequestBody request: ChangePasswordRequest) =
        userService.changePassword(
            1,
            request.oldPassword,
            request.password1,
            request.password2,
            null,
            null
        )

    @ServiceLog(description = "上传文件", printRequest = false)
    @PostMapping("/uploadFile")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(
        summary = "上传文件",
        description = "将用户上传的文件保存在静态文件目录static/file/\${user_id}/\${file_name}下"
    )
    fun uploadFile(
        @RequestParam @Parameter(
            description = "文件",
            required = true
        ) file: MultipartFile
    ) = userService.uploadFile(file)

    @ServiceLog(description = "删除文件")
    @PostMapping("/deleteFile")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(
        summary = "删除文件",
        description = "删除用户上传的文件, 使分享链接失效"
    )
    fun deleteFile(@RequestBody request: DeleteFileRequest) = userService.deleteFile(request.url)

    @ServiceLog(description = "修改用户信息")
    @PostMapping("/modifyUserInfo")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(
        summary = "修改用户信息",
        description = "未填写的信息则保持原样不变，注意：此接口无法设置\"管理员可用\"字段"
    )
    fun modifyUserInfo(@RequestBody request: ModifyUserInfoRequest): Response<Unit> {
        request.id = null
        request.email = null
        request.isDeleted = null
        return userService.modifyUserInfo(request)
    }

    @ServiceLog(description = "修改邮箱")
    @PostMapping("/modifyEmail")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(
        summary = "修改邮箱",
        description = "需要进行新邮箱验证和密码验证, 新邮箱验证发送验证码使用注册验证码接口即可"
    )
    fun modifyEmail(@RequestBody request: ModifyEmailRequest) =
        userService.modifyEmail(request.email, request.verifyCode, request.password)

    @ServiceLog(description = "查询用户列表表头")
    @GetMapping("/queryHeaders")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(
        summary = "查询用户列表表头",
        description = "查询用户列表表头"
    )
    fun queryHeaders() = userService.queryHeaders()

    @ServiceLog(description = "查询用户列表")
    @GetMapping("/list")
    @AuthCheck(auth = [AuthEnum.TOKEN, AuthEnum.ADMIN])
    @Tag(name = "用户接口")
    @Operation(
        summary = "查询用户列表",
        description = "查询用户列表"
    )
    fun list(
        @RequestParam("realName", required = false) @Parameter(description = "真实姓名，模糊") realName: String?,
        @RequestParam("gender", required = false) @Parameter(description = "性别，精确") gender: String?,
        @RequestParam("birthday", required = false) @Parameter(description = "生日，yyyy-MM-dd，精确") birthday: String?,
        @RequestParam("location", required = false) @Parameter(description = "位置，模糊") location: String?,
        @RequestParam("studentId", required = false) @Parameter(description = "学号，精确") studentId: String?,
        @RequestParam("classId", required = false) @Parameter(description = "班级，精确") classId: String?,
        @RequestParam(
            "admissionYear",
            required = false
        ) @Parameter(description = "入学时间，yyyy，精确") admissionYear: String?,
        @RequestParam(
            "graduationYear",
            required = false
        ) @Parameter(description = "毕业时间，yyyy，精确") graduationYear: String?,
        @RequestParam("college", required = false) @Parameter(description = "学院，精确") college: String?,
        @RequestParam("industry", required = false) @Parameter(description = "行业，精确") industry: String?,
        @RequestParam("company", required = false) @Parameter(description = "公司，精确") company: String?,
        @RequestParam("page") @Parameter(
            description = "页码, 从1开始",
            required = true
        ) @Valid @Min(1) page: Int = 1,
        @RequestParam("limit") @Parameter(
            description = "每页数量, 大于0",
            required = true
        ) @Valid @Min(1) limit: Int = 10
    ) = userService.list(
        realName,
        gender,
        birthday,
        location,
        studentId,
        classId,
        admissionYear,
        graduationYear,
        college,
        industry,
        company,
        page,
        limit
    )

    @ServiceLog(description = "用户列表下载", printResponse = false)
    @GetMapping("/download")
    @AuthCheck(auth = [AuthEnum.TOKEN, AuthEnum.ADMIN])
    @Tag(name = "用户接口")
    @Operation(
        summary = "用户列表下载",
        description = "用户列表下载"
    )
    fun download(): ResponseEntity<ByteArray> {
        try {
            userService.download().use { workbook ->
                ByteArrayOutputStream().use { os ->
                    workbook.write(os)
                    return DownloadUtils.downloadFile("user_${LocalDateTime.now()}.xlsx", os.toByteArray())
                }
            }
        } catch (e: Exception) {
            log.error("[UserController.download] failed to download users info", e)
            throw e
        }
    }

    @ServiceLog(description = "用户上传模板下载", printResponse = false)
    @GetMapping("/downloadTemplate")
    @AuthCheck(auth = [AuthEnum.TOKEN, AuthEnum.ADMIN])
    @Tag(name = "用户接口")
    @Operation(
        summary = "用户上传模板下载",
        description = "用户上传模板下载"
    )
    fun downloadTemplate(): ResponseEntity<ByteArray> {
        try {
            userService.downloadTemplate().use { wb ->
                ByteArrayOutputStream().use { os ->
                    wb.write(os)
                    return DownloadUtils.downloadFile("user_template.xlsx", os.toByteArray())
                }
            }
        } catch (e: Exception) {
            log.error("[UserController.downloadTemplate] failed to download users template", e)
            throw e
        }
    }

    @ServiceLog(description = "管理员上传用户信息", printRequest = false)
    @PostMapping("/upload")
    @AuthCheck(auth = [AuthEnum.TOKEN, AuthEnum.ADMIN])
    @Tag(name = "用户接口")
    @Operation(
        summary = "管理员上传用户信息",
        description = "管理员上传用户信息"
    )
    fun upload(@Valid request: UserUploadRequest): Response<Unit> {
        return userService.upload(request.file!!)
    }

    @ServiceLog(description = "根据关键词查询用户信息")
    @GetMapping("/findByKeyword")
    @AuthCheck
    @Tag(name = "用户接口")
    @Operation(summary = "根据关键词查询用户信息", description = "根据关键词查询用户信息，最多返回10个")
    fun findByKeyword(
        @RequestParam("keyword") @Parameter(
            description = "关键词",
            required = true
        ) @Valid @NotNull keyword: String?
    ) = userService.findByKeyword(keyword!!)

    @ServiceLog(description = "管理员修改用户信息")
    @PostMapping("/modifyUserInfoAdvanced")
    @AuthCheck(auth = [AuthEnum.TOKEN, AuthEnum.ADMIN])
    @Tag(name = "用户接口")
    @Operation(
        summary = "修改指定用户信息",
        description = "未填写的信息则保持原样不变"
    )
    fun modifyUserInfoAdvanced(@RequestBody request: ModifyUserInfoRequest) =
        userService.modifyUserInfo(request)
}