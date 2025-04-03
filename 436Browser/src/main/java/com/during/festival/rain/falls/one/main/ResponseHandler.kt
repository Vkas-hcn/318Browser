package com.during.festival.rain.falls.one.main


import android.util.Base64
import com.during.festival.rain.falls.one.appbean.BrowserAllBean
import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.utils.AppPointData
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

object ResponseHandler {

    fun handleAdminResponse(response: okhttp3.Response): Result<String> {
        return try {
            if (!response.isSuccessful) {
                AppPointData.getadmin(11, response.code.toString())
                return Result.failure(Exception("HTTP error: ${response.code}"))
            }

            val responseString = response.body?.string() ?: throw IllegalArgumentException("Response body is null")
            val datetime = response.header("datetime")
                ?: throw IllegalArgumentException("Missing datetime header")

            // 解密处理
            val decodedBytes = Base64.decode(responseString, Base64.DEFAULT)
            val decodedStr = String(decodedBytes, StandardCharsets.UTF_8)
            val finalData = xorEncrypt(decodedStr, datetime)

            // 解析数据
            val jsonResponse = JSONObject(finalData)
            val jsonData = parseAdminRefData(jsonResponse.toString())
            val adminBean = runCatching {
                Gson().fromJson(jsonData, BrowserAllBean::class.java)
            }.getOrNull()

            when {
                adminBean == null -> {
                    AppPointData.getadmin(7, null)
                    Result.failure(Exception("Invalid response format"))
                }

                IntBorApp.getAdminData() == null -> {
                    IntBorApp.putAdminData(jsonData)
                    val code = when {
                        adminBean.applicationSettings.userProfile.tier == 1 -> 1
                        else -> 2
                    }
                    AppPointData.getadmin(code, response.code.toString())
                    Result.success(jsonData)
                }

                adminBean.applicationSettings.userProfile.tier == 1 -> {
                    IntBorApp.putAdminData(jsonData)
                    AppPointData.getadmin(1, response.code.toString())
                    Result.success(jsonData)
                }

                else -> {
                    AppPointData.getadmin(2, response.code.toString())
                    Result.success(jsonData)
                }
            }
        } catch (e: Exception) {
            AppPointData.getadmin(3, "parse_error")
            Result.failure(e)
        } finally {
            response.close()
        }
    }

     fun xorEncrypt(text: String, datetime: String): String {
        val cycleKey = datetime.toCharArray()
        val keyLength = cycleKey.size
        return text.mapIndexed { index, char ->
            char.toInt().xor(cycleKey[index % keyLength].toInt()).toChar()
        }.joinToString("")
    }

    private fun parseAdminRefData(jsonString: String): String {
        return try {
            JSONObject(jsonString).getJSONObject("eieASEWD").getString("conf")
        } catch (e: Exception) {
            ""
        }
    }
}
