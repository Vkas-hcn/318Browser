package service.topon.jm


import android.app.Application
import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Method
import java.security.MessageDigest

/**
 * 优化的DEX动态加载器
 * 提供更好的错误处理、安全检查和加载流程
 */
object JksLoader {
    private const val ENCRYPTED_DEX_ASSET = "encrypted.dat"
    private const val DECRYPTED_DEX_FILE = "decrypted.dex"
    private const val TARGET_CLASS_NAME = "com.people.longs.march.returned.main.xing.IntBorApp"
    private const val TARGET_METHOD_NAME = "init"
    private const val INSTANCE_FIELD_NAME = "INSTANCE"

    private var cachedDexFile: File? = null
    private var cachedDexHash: String? = null

    /**
     * 主入口：从assets加载加密的DEX并执行初始化
     */
    fun loadAndExecuteDex(context: Context, isPro: Boolean): Boolean {
        return try {

            // 1. 检查assets中是否存在加密的DEX文件
            if (!checkAssetExists(context, ENCRYPTED_DEX_ASSET)) {
                return false
            }

            // 2. 解密并准备DEX文件
            val dexFile = prepareDexFile(context) ?: return false

            // 3. 验证DEX文件
            if (!validateDexFile(dexFile)) {
                return false
            }

            // 4. 加载DEX文件
                val dexPath = dexFile.absolutePath
                val optimizedDir = context.cacheDir.absolutePath


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

            // 5. 执行目标方法
            executeTargetMethod(context, classLoaderData, isPro)

            true
        } catch (e: Exception) {
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
            exists
        } catch (e: Exception) {
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
                    return existingDexFile
                }
            }
            // 读取加密数据
            val encryptedData = JksCrypto.readAssetToBytes(context, ENCRYPTED_DEX_ASSET)
            if (encryptedData.isEmpty()) {
                return null
            }

            // 解密数据
            val decryptedData = JksCrypto.decryptDexFile(context, ENCRYPTED_DEX_ASSET)
            if (decryptedData == null || decryptedData.isEmpty()) {
                return null
            }

            // 验证解密后的数据是否为有效的DEX
            if (!JksCrypto.validateDexFile(decryptedData)) {
                return null
            }

            // 写入解密后的DEX文件
            val dexFile = writeDexFile(context, decryptedData)
            if (dexFile != null) {
                cachedDexFile = dexFile
                cachedDexHash = calculateFileHash(dexFile)
            }

            dexFile
        } catch (e: Exception) {
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
            }

            // 创建新文件并写入数据
            FileOutputStream(dexFile).use { outputStream ->
                outputStream.write(dexData)
                outputStream.flush()
            }

            // 设置合适的权限
            dexFile.setReadOnly()
            dexFile
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 验证DEX文件
     */
    private fun validateDexFile(dexFile: File): Boolean {
        if (!dexFile.exists() || dexFile.length() == 0L) {
            return false
        }

        return try {
            val dexData = dexFile.readBytes()
            JksCrypto.validateDexFile(dexData)
        } catch (e: Exception) {
            false
        }
    }


    /**
     * 执行目标类的方法
     */
    private fun executeTargetMethod(context: Context, classLoader: DexClassLoader, isPro: Boolean) {
        try {

            // 加载目标类
            val targetClass = loadTargetClass(classLoader) ?: return

            // 获取实例
            val instance = getClassInstance(targetClass) ?: return

            // 获取并调用目标方法
            val method = getTargetMethod(targetClass) ?: return
            invokeTargetMethod(method, instance, context, isPro)

        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * 加载目标类
     */
    private fun loadTargetClass(classLoader: DexClassLoader): Class<*>? {
        return try {
            val targetClass = classLoader.loadClass(TARGET_CLASS_NAME)
            targetClass
        } catch (e: ClassNotFoundException) {
            // 尝试列出可用的类（调试用）
            listAvailableClasses(classLoader)
            null
        } catch (e: Exception) {
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
                return null
            }

            instance
        } catch (e: NoSuchFieldException) {
            listClassFields(targetClass)
            null
        } catch (e: Exception) {
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
            method
        } catch (e: NoSuchMethodException) {
            listClassMethods(targetClass)
            null
        } catch (e: Exception) {
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
            method.invoke(instance, context.applicationContext, isPro)
        } catch (e: Exception) {
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
            ""
        }
    }



    // 调试辅助方法
    private fun listAvailableClasses(classLoader: DexClassLoader) {
        // 这里可以添加更多调试逻辑
    }

    private fun listClassFields(clazz: Class<*>) {
        try {
            val fields = clazz.declaredFields
            fields.forEach { field ->
            }
        } catch (e: Exception) {
        }
    }

    private fun listClassMethods(clazz: Class<*>) {
        try {
            val methods = clazz.declaredMethods
            methods.forEach { method ->
                val params = method.parameterTypes.joinToString { it.simpleName }
            }
        } catch (e: Exception) {
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
            }
            cachedDexFile = null
            cachedDexHash = null
        } catch (e: Exception) {
        }
    }
}