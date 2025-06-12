package service.topon.jm


import android.content.Context
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 优化的DEX文件加密解密工具类
 * 使用AES-GCM模式提供更好的安全性
 */
object DexCrypto {
    private const val TAG = "DexCrypto"
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_IV_LENGTH = 12 // GCM推荐的IV长度
    private const val GCM_TAG_LENGTH = 16 // GCM认证标签长度
    private const val KEY_LENGTH = 256 // AES-256

    // 使用更复杂的密钥，实际应用中应该从安全的地方获取
    private val encryptionKey = "MyS3cur3K3yF0rD3xEncrypt!@#$%^&*"

    /**
     * 从assets读取文件内容
     */
    @Throws(IOException::class)
    fun readAssetToBytes(context: Context, assetName: String): ByteArray {
        return try {
            context.assets.open(assetName).use { inputStream ->
                val buffer = ByteArrayOutputStream()
                val data = ByteArray(8192) // 优化缓冲区大小
                var bytesRead: Int
                while (inputStream.read(data).also { bytesRead = it } != -1) {
                    buffer.write(data, 0, bytesRead)
                }
                buffer.toByteArray()
            }
        } catch (e: IOException) {
            Log.e(TAG, "读取assets文件失败: $assetName", e)
            throw e
        }
    }

    /**
     * 使用AES-GCM加密数据
     * @param data 要加密的数据
     * @param key 加密密钥
     * @return 加密后的数据 (IV + 加密数据 + 认证标签)
     */
    @Throws(Exception::class)
    fun encryptAESGCM(data: ByteArray, key: String): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key.toByteArray().copyOf(32), ALGORITHM) // 确保32字节
            val cipher = Cipher.getInstance(TRANSFORMATION)

            // 生成随机IV
            val iv = ByteArray(GCM_IV_LENGTH)
            SecureRandom().nextBytes(iv)

            val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)

            val encryptedData = cipher.doFinal(data)

            // 将IV和加密数据组合在一起
            val result = ByteArray(iv.size + encryptedData.size)
            System.arraycopy(iv, 0, result, 0, iv.size)
            System.arraycopy(encryptedData, 0, result, iv.size, encryptedData.size)

            Log.d(TAG, "数据加密成功，原始大小: ${data.size}, 加密后大小: ${result.size}")
            result
        } catch (e: Exception) {
            Log.e(TAG, "AES-GCM加密失败", e)
            throw e
        }
    }

    /**
     * 使用AES-GCM解密数据
     * @param encryptedData 加密的数据 (包含IV)
     * @param key 解密密钥
     * @return 解密后的原始数据
     */
    @Throws(Exception::class)
    fun decryptAESGCM(encryptedData: ByteArray, key: String): ByteArray {
        return try {
            if (encryptedData.size < GCM_IV_LENGTH + GCM_TAG_LENGTH) {
                throw IllegalArgumentException("加密数据长度不足")
            }

            val keySpec = SecretKeySpec(key.toByteArray().copyOf(32), ALGORITHM)
            val cipher = Cipher.getInstance(TRANSFORMATION)

            // 提取IV
            val iv = ByteArray(GCM_IV_LENGTH)
            System.arraycopy(encryptedData, 0, iv, 0, GCM_IV_LENGTH)

            // 提取加密数据
            val cipherData = ByteArray(encryptedData.size - GCM_IV_LENGTH)
            System.arraycopy(encryptedData, GCM_IV_LENGTH, cipherData, 0, cipherData.size)

            val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)

            val decryptedData = cipher.doFinal(cipherData)
            Log.d(TAG, "数据解密成功，解密后大小: ${decryptedData.size}")
            decryptedData
        } catch (e: Exception) {
            Log.e(TAG, "AES-GCM解密失败", e)
            throw e
        }
    }

    /**
     * 安全地写入文件
     */
    @Throws(IOException::class)
    fun writeSecureFile(data: ByteArray, filePath: String): Boolean {
        return try {
            FileOutputStream(filePath).use { fos ->
                fos.write(data)
                fos.flush()
            }
            Log.d(TAG, "文件写入成功: $filePath, 大小: ${data.size}")
            true
        } catch (e: IOException) {
            Log.e(TAG, "文件写入失败: $filePath", e)
            false
        }
    }

    /**
     * 生成加密的DEX文件并保存
     */
    fun generateEncryptedDex(context: Context, sourceAsset: String = "classes.dex"): Boolean {
        return try {
            Log.d(TAG, "开始生成加密DEX文件")

            // 读取原始DEX文件
            val dexBytes = readAssetToBytes(context, sourceAsset)
            if (dexBytes.isEmpty()) {
                Log.e(TAG, "DEX文件为空")
                return false
            }

            // 加密DEX内容
            val encryptedData = encryptAESGCM(dexBytes, encryptionKey)

            // 保存加密文件
            val encryptedFileName = "${context.filesDir}/encrypted.dat"
            val success = writeSecureFile(encryptedData, encryptedFileName)

            if (success) {
                Log.d(TAG, "加密DEX文件生成成功: $encryptedFileName")
            }
            success
        } catch (e: Exception) {
            Log.e(TAG, "生成加密DEX文件失败", e)
            false
        }
    }

    /**
     * 解密DEX文件
     */
    fun decryptDexFile(context: Context, encryptedFileName: String = "encrypted.dat"): ByteArray? {
        return try {
            Log.d(TAG, "开始解密DEX文件")

            // 读取加密文件
            val encryptedData = readAssetToBytes(context, encryptedFileName)
            if (encryptedData.isEmpty()) {
                Log.e(TAG, "加密文件为空或不存在")
                return null
            }

            // 解密数据
            val decryptedData = decryptAESGCM(encryptedData, encryptionKey)
            Log.d(TAG, "DEX文件解密成功")
            decryptedData
        } catch (e: Exception) {
            Log.e(TAG, "解密DEX文件失败", e)
            null
        }
    }

    /**
     * 验证DEX文件头部
     */
    fun validateDexFile(dexData: ByteArray): Boolean {
        if (dexData.size < 8) return false

        // 检查DEX魔数
        val dexMagic = "dex\n"
        val header = String(dexData.copyOfRange(0, 4))
        val isValid = header == dexMagic

        Log.d(TAG, "DEX文件验证结果: $isValid")
        return isValid
    }

    /**
     * 生成随机密钥（用于更高安全性场景）
     */
    fun generateRandomKey(): String {
        val keyGen = KeyGenerator.getInstance(ALGORITHM)
        keyGen.init(KEY_LENGTH)
        val secretKey = keyGen.generateKey()
        return Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)
    }
}