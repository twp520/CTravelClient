package com.colin.ctravel.util

import android.content.Context
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import java.io.File


object OSSManager {
    private var ossClient: OSSClient? = null

    fun init(context: Context) {
        val accessKeyId = "LTAIrr1PPwsT7Oxr"
        val accessKeySecret = "rfQRPI9jofJxJVrSbn19SY638aj3Px"
        val credentialProvider = OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, "")
        //该配置类如果不设置，会有默认配置，具体可看该类
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
        conf.maxConcurrentRequest = 5 // 最大并发请求数，默认5个
        conf.maxErrorRetry = 3 // 失败后最大重试次数
        val endpoint = "oss-cn-beijing.aliyuncs.com"
        ossClient = OSSClient(context, endpoint, credentialProvider)
    }

    @Throws(ClientException::class, ServiceException::class)
    fun upLoadFileSync(file: File): String {
        // 构造上传请求
        val bucket = "colin-ctravel-pictuer"
        // 构造上传请求
        val put = PutObjectRequest(bucket, "photo/" + file.name, file.absolutePath)
        ossClient!!.putObject(put)
        return getFileUrl(file.name)
    }


    private fun getFileUrl(fileName: String): String {
        val fileUrlFix = "https://colin-ctravel-pictuer.oss-cn-beijing.aliyuncs.com/photo/"
        return fileUrlFix + fileName
    }


}
