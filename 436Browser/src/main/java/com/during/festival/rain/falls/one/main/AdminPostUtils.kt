package com.during.festival.rain.falls.one.main

import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.during.festival.rain.falls.one.utils.AppPointData
import com.during.festival.rain.falls.one.utils.PngAllData
import com.during.festival.rain.falls.one.utils.TtPoint
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object AdminPostUtils {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun executeAdminRequest(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val requestData = prepareRequestData()
                IntBorApp.showLog("executeAdminRequest=$requestData")
                TtPoint.postPointData(false, "reqadmin")

                val (processedData, datetime) = processRequestData(requestData)
                val targetUrl = PngAllData.getConfig().adminUrl
                Log.e(
                    "TAG",
                    "SanZong.ADMIN_URL=${IntBorApp.mustXS}=: $targetUrl",
                )

                val request = Request.Builder()
                    .url(targetUrl)
                    .post(RequestBody.create("application/json".toMediaTypeOrNull(), processedData))
                    .addHeader("datetime", datetime)
                    .build()

                val response = client.newCall(request).execute()
                ResponseHandler.handleAdminResponse(response)
            } catch (e: SocketTimeoutException) {
                Result.failure(Exception("Request timed out: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Operation failed: ${e.message}"))
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun prepareRequestData(): String {
        return JSONObject().apply {
            put("uhYyV", "com.purplemark.browser.fast.secure")
            put("RrCH", IntBorApp.okSpBean.appiddata)
            put("aRXz", IntBorApp.okSpBean.refdata)
//            put("aRXz", "fb4a")
            put("SjQa", AppPointData.showAppVersion())
        }.toString()
    }

    private fun processRequestData(rawData: String): Pair<String, String> {
        val datetime = System.currentTimeMillis().toString()
        val encrypted = ResponseHandler.xorEncrypt(rawData, datetime)
        return Base64.encodeToString(encrypted.toByteArray(), Base64.NO_WRAP) to datetime
    }


    suspend fun executePutRequest(body: Any): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // 准备请求数据
                val jsonBodyString = JSONObject(body.toString()).toString()
                val targetUrl = PngAllData.getConfig().upUrl

                // 配置连接
                val request = Request.Builder()
                    .url(targetUrl)
                    .post(RequestBody.create("application/json".toMediaTypeOrNull(), jsonBodyString))
                    .build()

                // 发送请求
                val response = client.newCall(request).execute()

                // 处理响应
                handlePutResponse(response)
            } catch (e: Exception) {
                Result.failure(Exception("Put request failed: ${e.message}"))
            }
        }
    }

    private fun handlePutResponse(response: Response): Result<String> {
        return try {
            if (!response.isSuccessful) {
                return Result.failure(Exception("HTTP error: ${response.code}"))
            }

            val responseString = response.body?.string() ?: throw IllegalArgumentException("Response body is null")
            Result.success(responseString)
        } catch (e: Exception) {
            Result.failure(Exception("Response processing failed: ${e.message}"))
        } finally {
            response.close()
        }
    }
}
