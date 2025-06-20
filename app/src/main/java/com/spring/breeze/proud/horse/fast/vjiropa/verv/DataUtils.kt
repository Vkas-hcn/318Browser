package com.spring.breeze.proud.horse.fast.vjiropa.verv

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast

object DataUtils {
    val pp_url = "https://sites.google.com/view/cloud-view-browser/home"
    const val ccckk_url = "https://faustian.browserstable.com/guignol/electra/plane"

    const val ins_url = "https://www.instagram.com/"
    const val vimor_url = "https://www.vimeo.com/"
    const val tiktok_url = "https://www.tiktok.com/"
    const val fb_url = "https://www.facebook.com/"

    const val ytb_url = "https://www.youtube.com/"
    const val massager_url = "https://www.messenger.com/"
    const val whatsapp_url = "https://www.whatsapp.com/"
    const val discord_url = "https://www.discord.com/"

    const val ser_google = "https://www.google.com/search?q="
    const val ser_bing = "https://www.bing.com/search?q="
    const val ser_yahoo = "https://search.yahoo.com/search?p="
    const val ser_duck = "https://duckduckgo.com/?t=h_&q="

    private const val FILENAME_HISTORY = "web_page_history.json"
    private const val FILENAME_WEB = "web_page_tba.json"

    val web_page_tba = """
[
    {
        "id": "0",
        "webImage": "",
        "webUrl": "",
        "showPage": 1
    }
]
""".trimIndent()




    fun copyUrl(context: Context, url: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        clipboardManager.setPrimaryClip(android.content.ClipData.newPlainText("url", url))
        Toast.makeText(context, "The replication is successful", Toast.LENGTH_SHORT).show()
    }

    // 保存数据到文件
    fun saveWebPageInfoList(
        context: Context,
        webPageInfoList: MutableList<WkvrnBean>
    ) {
        val file = File(context.filesDir,
            FILENAME_HISTORY
        )
        val json = Gson().toJson(webPageInfoList)

        FileOutputStream(file).use { fos ->
            OutputStreamWriter(fos).use { osw ->
                osw.write(json)
            }
        }
    }

    // 从文件中加载数据
    fun loadWebPageInfoList(
        context: Context
    ): MutableList<WkvrnBean>? {

        val file = File(context.filesDir,
            FILENAME_HISTORY
        )
        if (!file.exists()) return null

        val json = FileInputStream(file).use { fis ->
            InputStreamReader(fis).use { isr ->
                isr.readText()
            }
        }

        val typeToken = object : TypeToken<MutableList<WkvrnBean>>() {}.type
        return Gson().fromJson(json, typeToken)
    }


    fun saveWebTabList(
        context: Context,
        tbasWebBeanList: MutableList<TbasWebBean>
    ) {
        val file = File(context.filesDir,
            FILENAME_WEB
        )
        val json = Gson().toJson(tbasWebBeanList)

        FileOutputStream(file).use { fos ->
            OutputStreamWriter(fos).use { osw ->
                osw.write(json)
            }
        }
    }

    fun loadWebTabList(
        context: Context
    ): MutableList<TbasWebBean> {
        val file = File(context.filesDir,
            FILENAME_WEB
        )
        if (!file.exists()) return narData()

        val json = FileInputStream(file).use { fis ->
            InputStreamReader(fis).use { isr ->
                isr.readText()
            }
        }

        // 显式指定泛型类型并确保类型信息被正确捕获
        val list = Gson().fromJson<MutableList<TbasWebBean>>(
            json,
            object : TypeToken<MutableList<TbasWebBean>>() {}.type
        )

        // 排序逻辑保持不变
        return list.sortedByDescending { it.id.toLong() }
            .toMutableList()
    }


    fun removeWebTabById(context: Context, id: String,finishFun:()->Unit) {
        val list = loadWebTabList(context)
        // 过滤出不匹配id的数据
        val filteredList = list.filter { it.id != id }.toMutableList()

        // 如果数据有变化则保存
         if (filteredList.size < list.size) {
             saveWebTabList(
                 context,
                 filteredList
             )
             finishFun()
        }
    }
    fun findWebBeanById(context: Context, id: String): TbasWebBean? {
        val list = loadWebTabList(context) ?: return null
        return list.firstOrNull { it.id == id }
    }


    fun narData():MutableList<TbasWebBean>{
        val typeToken = object : TypeToken<MutableList<TbasWebBean>>() {}.type
        return Gson().fromJson(web_page_tba, typeToken)
    }



    fun saveScreenshot(context: Context, view: View): String {
        // 创建位图
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        ).apply {
            val canvas = android.graphics.Canvas(this)
            view.draw(canvas)
        }

        // 生成唯一文件名（带时间戳）
        val fileName = "screenshot_${System.currentTimeMillis()}.png"
        val file = File(context.filesDir, fileName)

        return try {
            // 保存位图到文件
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            // 返回文件路径作为持久化链接
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        } finally {
            bitmap.recycle()
        }
    }


}
