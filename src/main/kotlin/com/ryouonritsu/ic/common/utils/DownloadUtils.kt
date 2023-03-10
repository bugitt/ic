package com.ryouonritsu.ic.common.utils

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

/**
 * @author ryouonritsu
 */
object DownloadUtils {
    fun getResponseEntity(
        fileBytes: ByteArray,
        fileName: String,
        inline: Boolean,
        mediaType: MediaType
    ): ResponseEntity<ByteArray> {
        val contentDisposition = HttpHeaderValueUtils.getContentDisposition(fileName, inline)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
            .header(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,must-revalidate")
            .header(HttpHeaders.EXPIRES, "0")
            .contentLength(fileBytes.size.toLong())
            .contentType(mediaType)
            .body(fileBytes)
    }

    fun downloadFile(fileName: String, data: ByteArray): ResponseEntity<ByteArray> {
        return getResponseEntity(data, fileName, false, MediaType.APPLICATION_OCTET_STREAM)
    }
}