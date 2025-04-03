package com.during.festival.rain.falls.one.utils

import android.content.Context
import com.anythink.core.api.ATAdInfo
import com.during.festival.rain.falls.one.utils.AppPointData.upAdJson
import com.during.festival.rain.falls.one.utils.AppPointData.upInstallJson
import com.during.festival.rain.falls.one.utils.AppPointData.upPointJson
import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.main.IntBorApp.canIntNextFun
import com.during.festival.rain.falls.one.main.IntBorApp.mainStart
import com.during.festival.rain.falls.one.main.AdminPostUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException
import kotlin.random.Random
import kotlin.coroutines.resume

object TtPoint {
    // 在类顶部添加协程作用域
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    sealed class Result<out T> {
        data class Success<out T>(val value: T) : Result<T>()
        data class Failure(val exception: Throwable) : Result<Nothing>()
    }

    fun onePostAdmin() {
        scope.launch {
            IntBorApp.showLog("无数据启动请求:")
            executeWithRetry(
                maxRetries = 3,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = executeAdminRequestSuspend()
                    val bean = IntBorApp.getAdminData()
                    IntBorApp.showLog("admin-data-1: $result")
                    if (bean != null && bean.applicationSettings.userProfile.tier != 1) {
                        IntBorApp.showLog("不是A用户，进行重试")
                        bPostAdmin()
                    }
                    if (bean?.applicationSettings?.userProfile?.tier == 1) {
                        canIntNextFun()
                    }
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(3, "Admin", e.message ?: "", attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    fun twoPostAdmin() {
        IntBorApp.showLog("有数据启动请求:")
        scope.launch {
            val bean = IntBorApp.getAdminData()
            val delay = Random.nextLong(1000, 10 * 60 * 1000)
            var isStart = false

            if (bean != null && bean.applicationSettings.userProfile.tier == 1) {
                isStart = true
                canIntNextFun()
                IntBorApp.showLog("冷启动app延迟 ${delay}ms 请求admin数据")
                delay(delay)
            }
            executeWithRetry(
                maxRetries = 3,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = executeAdminRequestSuspend()
                    val bean = IntBorApp.getAdminData()
                    IntBorApp.showLog("admin-data-2: $result")
                    if (bean != null && bean.applicationSettings.userProfile.tier != 1) {
                        IntBorApp.showLog("不是A用户，进行重试")
                        bPostAdmin()
                    }
                    if (bean?.applicationSettings?.userProfile?.tier == 1 && !isStart) {
                        canIntNextFun()
                    }
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(3, "Admin", e.message ?: "", attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    private fun bPostAdmin() {
        scope.launch {
            delay(50_000L)
            executeWithRetry(
                maxRetries = 20,
                minDelay = 59_000L,
                maxDelay = 60_000L
            ) { attempt ->
                try {
                    val result = executeAdminRequestSuspend()
                    val bean = IntBorApp.getAdminData()
                    IntBorApp.showLog("admin-onSuccess: $result")
                    if (bean != null && bean.applicationSettings.userProfile.tier != 1) {
                        handleError(20, "不是A用户，进行重试", "", attempt)
                        Result.Failure(Exception())
                    } else {
                        canIntNextFun()
                        Result.Success(Unit)
                    }
                } catch (e: Exception) {
                    handleError(20, "Admin", e.message ?: "", attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    fun postInstallData(context: Context) {
        scope.launch {
            val data = withContext(Dispatchers.Default) {
                IntBorApp.okSpBean.IS_INT_JSON.ifEmpty {
                    val newData = upInstallJson(context)
                    IntBorApp.okSpBean.IS_INT_JSON = newData
                    newData
                }
            }

            IntBorApp.showLog("Install: data=$data")

            // 执行带重试机制的请求
            executeWithRetry(
                maxRetries = 20,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = executePutRequestSuspend(data)
                    handleSuccess("Install", result)
                    IntBorApp.okSpBean.IS_INT_JSON = ""
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(20, "Install", e.message ?: "", attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    fun postAdData(adValue: ATAdInfo) {
        scope.launch {
            // 准备请求数据
            val data = upAdJson(mainStart, adValue)
            IntBorApp.showLog("postAd: data=$data")
            // 执行带重试机制的请求
            executeWithRetry(
                maxRetries = 3,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = executePutRequestSuspend(data)
                    handleSuccess("Ad", result)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(3, "Ad", e.message ?: "", attempt)
                    Result.Failure(e)
                }
            }
            AppPointData.postAdValue(adValue)
        }
    }

    fun postPointData(
        isAdMinCon: Boolean,
        name: String,
        key1: String? = null,
        keyValue1: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null
    ) {
        scope.launch {
            val adminBean = IntBorApp.getAdminData()
            if (!isAdMinCon && (adminBean != null && !adminBean.applicationSettings.userProfile.syncEnabled)) {
                return@launch
            }
            // 准备请求数据
            val data = if (key1 != null) {
                upPointJson(name, key1, keyValue1, key2, keyValue2)
            } else {
                upPointJson(name)
            }
            IntBorApp.showLog("Point-${name}-开始打点--${data}")
            // 执行带重试机制的请求
            val maxNum = if (isAdMinCon) {
                20
            } else {
                3
            }
            executeWithRetry(
                maxRetries = maxNum,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = executePutRequestSuspend(data)
                    handleSuccess("Point-${name}", result)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(maxNum, "Point-${name}", e.message ?: "", attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    // 带随机延迟的重试执行器
    private suspend fun <T> executeWithRetry(
        maxRetries: Int,
        minDelay: Long,
        maxDelay: Long,
        block: suspend (attempt: Int) -> Result<T>
    ) {
        repeat(maxRetries) { attempt ->
            when (val result = block(attempt)) {
                is Result.Success -> return
                is Result.Failure -> {
                    val delayTime = Random.nextLong(minDelay, maxDelay)
                    delay(delayTime)
                }
            }
        }
    }

    private suspend fun executeAdminRequestSuspend(): String {
        return suspendCancellableCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                val result = AdminPostUtils.executeAdminRequest()
                if (result.isSuccess) {
                    continuation.resume(result.getOrNull() ?: "")

                } else {
                    continuation.resumeWithException(Exception(result.getOrNull() ?: ""))

                }
            }
        }
    }

    // 挂起函数扩展
    private suspend fun executePutRequestSuspend(data: String): String {
        return suspendCancellableCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                val result = AdminPostUtils.executePutRequest(data)
                if (result.isSuccess) {
                    continuation.resume(result.getOrNull() ?: "")

                } else {
                    continuation.resumeWithException(Exception(result.getOrNull() ?: ""))
                }
            }
        }
    }

    // 处理成功响应
    private fun handleSuccess(type: String, result: String) {
        IntBorApp.showLog("${type}-请求成功: $result")
    }

    // 处理错误日志
    private fun handleError(maxNum: Int, type: String, e: String, attempt: Int) {
        IntBorApp.showLog(
            """
        ${type}-请求失败[重试次数:${attempt + 1}]: ${e}
        ${if (attempt >= maxNum - 1) "达到最大重试次数" else ""}
    """.trimIndent()
        )
    }

}