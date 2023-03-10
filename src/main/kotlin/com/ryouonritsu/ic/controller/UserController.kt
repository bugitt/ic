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
        description = "未填写的信息则保持原样不变"
    )
    fun modifyUserInfo(@RequestBody request: ModifyUserInfoRequest) =
        userService.modifyUserInfo(request)

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
        @RequestParam("page") @Parameter(
            description = "页码, 从1开始",
            required = true
        ) @Valid @NotNull @Min(1) page: Int?,
        @RequestParam("limit") @Parameter(
            description = "每页数量, 大于0",
            required = true
        ) @Valid @NotNull @Min(1) limit: Int?
    ) = userService.list(page ?: 1, limit ?: 10)

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
}