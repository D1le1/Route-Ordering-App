package com.example.operatordesktop.util

import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64
import java.util.Properties

object CryptoUtils {
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private var key: ByteArray? = null // 32 символа для AES-256
    private var iv: ByteArray? = null// 16 символов

    // Шифрование
    fun encrypt(phone: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val keySpec = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(phone.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    // Дешифрование
    fun decrypt(encryptedPhone: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val keySpec = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decoded = Base64.getDecoder().decode(encryptedPhone)
        val decrypted = cipher.doFinal(decoded)
        return String(decrypted)
    }

    fun generateKeyAndIv(): Pair<ByteArray, ByteArray> {
        val key = ByteArray(32).apply { SecureRandom().nextBytes(this) } // AES-256
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }  // 16 байт для CBC
        return Pair(key, iv)
    }

    fun saveToProperties(key: ByteArray, iv: ByteArray, filePath: String) {
        val props = Properties().apply {
            setProperty("aes.key", Base64.getEncoder().encodeToString(key))
            setProperty("aes.iv", Base64.getEncoder().encodeToString(iv))
        }
        FileOutputStream(filePath).use { output ->
            props.store(output, "AES Configuration")
        }
    }

    fun loadFromProperties(filePath: String): Pair<ByteArray?, ByteArray?> {
        val props = Properties().apply {
            FileInputStream(filePath).use { input -> load(input) }
        }
        key = Base64.getDecoder().decode(props.getProperty("aes.key"))
        iv = Base64.getDecoder().decode(props.getProperty("aes.iv"))
        return Pair(key, iv)
    }

}