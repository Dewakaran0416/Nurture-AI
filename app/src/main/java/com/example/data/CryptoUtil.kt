package com.example.data

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoUtil {
    private const val PREFIX = "[AES_SECURE_V2]"
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    
    // Derive AES key using SHA-256
    private fun getSecretKey(keySource: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = keySource.toByteArray(StandardCharsets.UTF_8)
        val keyBytes = digest.digest(bytes)
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun getIv(): IvParameterSpec {
        // Standard static initialization vector for decentralized local portability
        val ivBytes = byteArrayOf(
            0x15, 0x32, 0x76, 0x09, 0x12, 0x48, 0x30, 0x12,
            0x29, 0x15, 0x42, 0x02, 0x51, 0x07, 0x05, 0x36
        )
        return IvParameterSpec(ivBytes)
    }

    /**
     * Encrypt a text value. Returns prefixed Base64 string if successful.
     */
    fun encrypt(text: String, keySource: String = "nurture_fallback_key"): String {
        if (text.isBlank()) return text
        if (text.startsWith(PREFIX)) return text // Already encrypted
        return try {
            val keySpec = getSecretKey(keySource)
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, getIv())
            val encryptedBytes = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
            val base64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
            "$PREFIX$base64"
        } catch (e: Exception) {
            text
        }
    }

    /**
     * Decrypt a text value. Returns original string if not encrypted.
     */
    fun decrypt(encryptedText: String, keySource: String = "nurture_fallback_key"): String {
        if (!encryptedText.startsWith(PREFIX)) return encryptedText // Not encrypted
        return try {
            val base64Part = encryptedText.substring(PREFIX.length)
            val encryptedBytes = Base64.decode(base64Part, Base64.NO_WRAP)
            val keySpec = getSecretKey(keySource)
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, getIv())
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            "[Decrypted Secure Asset]"
        }
    }
}
