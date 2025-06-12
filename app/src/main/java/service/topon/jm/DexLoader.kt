package service.topon.jm


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Method
import java.security.MessageDigest

/**
 * 优化的DEX动态加载器
 * 提供更好的错误处理、安全检查和加载流程
 */
@Keep
object DexLoader {
    private const val TAG = "DexLoader"
    private const val ENCRYPTED_DEX_ASSET = "encrypted.dat"
    private const val DECRYPTED_DEX_FILE = "decrypted.dex"
    private const val TARGET_CLASS_NAME = "com.people.longs.march.returned.main.xing.IntBorApp"
    private const val TARGET_METHOD_NAME = "init"
    private const val INSTANCE_FIELD_NAME = "INSTANCE"

    // DEX文件缓存和验证
    private var cachedDexFile: File? = null
    private var cachedDexHash: String? = null

    /**
     * 主入口：从assets加载加密的DEX并执行初始化
     */
    fun loadAndExecuteDex(context: Context, isPro: Boolean): Boolean {
        return try {
            Log.d(TAG, "开始DEX加载流程")

            // 1. 检查assets中是否存在加密的DEX文件
            if (!checkAssetExists(context, ENCRYPTED_DEX_ASSET)) {
                Log.e(TAG, "加密DEX文件不存在: $ENCRYPTED_DEX_ASSET")
                return false
            }

            // 2. 解密并准备DEX文件
            val dexFile = prepareDexFile(context) ?: return false

            // 3. 验证DEX文件
            if (!validateDexFile(dexFile)) {
                Log.e(TAG, "DEX文件验证失败")
                return false
            }

            // 4. 加载DEX文件
                val dexPath = dexFile.absolutePath
                val optimizedDir = context.cacheDir.absolutePath

                Log.d(TAG, "创建DexClassLoader")
                Log.d(TAG, "DEX路径: $dexPath")
                Log.d(TAG, "优化目录: $optimizedDir")

//            val classLoader = DexClassLoader(
//                dexPath,
//                optimizedDir,
//                null,
//                context.classLoader
//            )

                val dexClassLoaderClass = Class.forName("dalvik.system.DexClassLoader")
                val constructor = dexClassLoaderClass.getConstructor(
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    ClassLoader::class.java
                )
                val classLoaderData = constructor.newInstance(
                    dexPath,
                    context.cacheDir.path,
                    null,
                    context.classLoader
                ) as DexClassLoader
                Log.d(TAG, "DexClassLoader创建成功")

            // 5. 执行目标方法
            executeTargetMethod(context, classLoaderData, isPro)

            Log.d(TAG, "DEX加载和执行完成")
            true
        } catch (e: Exception) {
            Log.e(TAG, "DEX加载流程失败", e)
            handleLoadError(e)
            false
        }
    }

    /**
     * 检查asset文件是否存在
     */
    private fun checkAssetExists(context: Context, assetName: String): Boolean {
        return try {
            val assetList = context.assets.list("") ?: emptyArray()
            val exists = assetList.contains(assetName)
            Log.d(TAG, "Asset检查: $assetName, 存在: $exists")
            Log.d(TAG, "可用assets: ${assetList.joinToString()}")
            exists
        } catch (e: Exception) {
            Log.e(TAG, "检查asset文件失败", e)
            false
        }
    }

    /**
     * 准备DEX文件（解密并写入）
     */
    private fun prepareDexFile(context: Context): File? {
        return try {
            // 检查是否有缓存的DEX文件
            val existingDexFile = File(context.filesDir, DECRYPTED_DEX_FILE)
            if (existingDexFile.exists()) {
                val currentHash = calculateFileHash(existingDexFile)
                if (currentHash == cachedDexHash && cachedDexFile?.exists() == true) {
                    Log.d(TAG, "使用缓存的DEX文件")
                    return existingDexFile
                }
            }

            Log.d(TAG, "开始解密DEX文件")

            // 读取加密数据
            val encryptedData = DexCrypto.readAssetToBytes(context, ENCRYPTED_DEX_ASSET)
            if (encryptedData.isEmpty()) {
                Log.e(TAG, "读取加密数据失败")
                return null
            }

            // 解密数据
            val decryptedData = DexCrypto.decryptDexFile(context, ENCRYPTED_DEX_ASSET)
            if (decryptedData == null || decryptedData.isEmpty()) {
                Log.e(TAG, "解密DEX失败")
                return null
            }

            // 验证解密后的数据是否为有效的DEX
            if (!DexCrypto.validateDexFile(decryptedData)) {
                Log.e(TAG, "解密后的数据不是有效的DEX文件")
                return null
            }

            // 写入解密后的DEX文件
            val dexFile = writeDexFile(context, decryptedData)
            if (dexFile != null) {
                cachedDexFile = dexFile
                cachedDexHash = calculateFileHash(dexFile)
                Log.d(TAG, "DEX文件准备成功: ${dexFile.absolutePath}")
            }

            dexFile
        } catch (e: Exception) {
            Log.e(TAG, "准备DEX文件失败", e)
            null
        }
    }

    /**
     * 安全地写入DEX文件
     */
    private fun writeDexFile(context: Context, dexData: ByteArray): File? {
        val dexFile = File(context.filesDir, DECRYPTED_DEX_FILE)

        return try {
            // 删除旧文件
            if (dexFile.exists()) {
                val deleted = dexFile.delete()
                Log.d(TAG, "删除旧DEX文件: $deleted")
            }

            // 创建新文件并写入数据
            FileOutputStream(dexFile).use { outputStream ->
                outputStream.write(dexData)
                outputStream.flush()
            }

            // 设置合适的权限
            dexFile.setReadOnly()
            Log.d(TAG, "DEX文件写入成功: ${dexFile.absolutePath}, 大小: ${dexFile.length()}")
            dexFile
        } catch (e: Exception) {
            Log.e(TAG, "写入DEX文件失败", e)
            null
        }
    }

    /**
     * 验证DEX文件
     */
    private fun validateDexFile(dexFile: File): Boolean {
        if (!dexFile.exists() || dexFile.length() == 0L) {
            Log.e(TAG, "DEX文件不存在或为空")
            return false
        }

        return try {
            val dexData = dexFile.readBytes()
            DexCrypto.validateDexFile(dexData)
        } catch (e: Exception) {
            Log.e(TAG, "DEX文件验证异常", e)
            false
        }
    }


    /**
     * 执行目标类的方法
     */
    private fun executeTargetMethod(context: Context, classLoader: DexClassLoader, isPro: Boolean) {
        try {
            Log.d(TAG, "开始加载目标类: $TARGET_CLASS_NAME")

            // 加载目标类
            val targetClass = loadTargetClass(classLoader) ?: return

            // 获取实例
            val instance = getClassInstance(targetClass) ?: return

            // 获取并调用目标方法
            val method = getTargetMethod(targetClass) ?: return
            invokeTargetMethod(method, instance, context, isPro)

        } catch (e: Exception) {
            Log.e(TAG, "执行目标方法失败", e)
            throw e
        }
    }

    /**
     * 加载目标类
     */
    private fun loadTargetClass(classLoader: DexClassLoader): Class<*>? {
        return try {
            val targetClass = classLoader.loadClass(TARGET_CLASS_NAME)
            Log.d(TAG, "成功加载目标类: ${targetClass.name}")
            targetClass
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "找不到目标类: $TARGET_CLASS_NAME", e)
            // 尝试列出可用的类（调试用）
            listAvailableClasses(classLoader)
            null
        } catch (e: Exception) {
            Log.e(TAG, "加载目标类异常", e)
            null
        }
    }

    /**
     * 获取类实例
     */
    private fun getClassInstance(targetClass: Class<*>): Any? {
        return try {
            val instanceField = targetClass.getDeclaredField(INSTANCE_FIELD_NAME)
            instanceField.isAccessible = true
            val instance = instanceField.get(null)

            if (instance == null) {
                Log.e(TAG, "INSTANCE字段为null")
                return null
            }

            Log.d(TAG, "成功获取类实例: ${instance.javaClass.name}")
            instance
        } catch (e: NoSuchFieldException) {
            Log.e(TAG, "找不到INSTANCE字段", e)
            listClassFields(targetClass)
            null
        } catch (e: Exception) {
            Log.e(TAG, "获取类实例失败", e)
            null
        }
    }

    /**
     * 获取目标方法
     */
    private fun getTargetMethod(targetClass: Class<*>): Method? {
        return try {
            val method = targetClass.getDeclaredMethod(
                TARGET_METHOD_NAME,
                Application::class.java,
                Boolean::class.java
            )
            method.isAccessible = true
            Log.d(TAG, "成功获取目标方法: ${method.name}")
            method
        } catch (e: NoSuchMethodException) {
            Log.e(TAG, "找不到目标方法: $TARGET_METHOD_NAME", e)
            listClassMethods(targetClass)
            null
        } catch (e: Exception) {
            Log.e(TAG, "获取目标方法失败", e)
            null
        }
    }

    /**
     * 调用目标方法
     */
    private fun invokeTargetMethod(
        method: Method,
        instance: Any,
        context: Context,
        isPro: Boolean
    ) {
        try {
            Log.d(TAG, "准备调用目标方法: ${method.name}")
            method.invoke(instance, context.applicationContext, isPro)
            Log.d(TAG, "目标方法调用成功")
        } catch (e: Exception) {
            Log.e(TAG, "调用目标方法失败", e)
            throw e
        }
    }

    /**
     * 计算文件哈希值（用于缓存验证）
     */
    private fun calculateFileHash(file: File): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = file.readBytes()
            val hash = digest.digest(bytes)
            hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e(TAG, "计算文件哈希失败", e)
            ""
        }
    }

    /**
     * 错误处理
     */
    private fun handleLoadError(e: Exception) {
        when (e) {
            is ClassNotFoundException -> Log.e(TAG, "类加载失败: ${e.message}")
            is NoSuchFieldException -> Log.e(TAG, "字段不存在: ${e.message}")
            is NoSuchMethodException -> Log.e(TAG, "方法不存在: ${e.message}")
            is IllegalAccessException -> Log.e(TAG, "访问权限异常: ${e.message}")
            is SecurityException -> Log.e(TAG, "安全异常: ${e.message}")
            else -> Log.e(TAG, "未知异常: ${e.javaClass.simpleName} - ${e.message}")
        }
    }

    // 调试辅助方法
    private fun listAvailableClasses(classLoader: DexClassLoader) {
        Log.d(TAG, "尝试列出可用类（调试用）")
        // 这里可以添加更多调试逻辑
    }

    private fun listClassFields(clazz: Class<*>) {
        try {
            val fields = clazz.declaredFields
            Log.d(TAG, "类 ${clazz.name} 的所有字段:")
            fields.forEach { field ->
                Log.d(TAG, "  字段: ${field.name}, 类型: ${field.type.simpleName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "列出类字段失败", e)
        }
    }

    private fun listClassMethods(clazz: Class<*>) {
        try {
            val methods = clazz.declaredMethods
            Log.d(TAG, "类 ${clazz.name} 的所有方法:")
            methods.forEach { method ->
                val params = method.parameterTypes.joinToString { it.simpleName }
                Log.d(TAG, "  方法: ${method.name}($params)")
            }
        } catch (e: Exception) {
            Log.e(TAG, "列出类方法失败", e)
        }
    }

    /**
     * 清理临时文件
     */
    fun cleanup(context: Context) {
        try {
            val dexFile = File(context.filesDir, DECRYPTED_DEX_FILE)
            if (dexFile.exists()) {
                val deleted = dexFile.delete()
                Log.d(TAG, "清理DEX文件: $deleted")
            }
            cachedDexFile = null
            cachedDexHash = null
        } catch (e: Exception) {
            Log.e(TAG, "清理文件失败", e)
        }
    }
}