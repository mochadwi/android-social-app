package io.mochadwi.util.helper

import java.security.InvalidKeyException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class StringEncryptionTools @Throws(Exception::class)
constructor() {

    private var key: SecretKey? = null
    private var cipher: Cipher? = null

    init {
        init()
    }

    private fun hexToBytes(str: String?): ByteArray? {
        if (str == null) {
            return null
        } else if (str.length < 2) {
            return null
        } else {
            val len = str.length / 2
            val buffer = ByteArray(len)
            for (i in 0 until len) {
                buffer[i] = Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16).toByte()
            }
            return buffer
        }
    }

    private fun bytesToHex(data: ByteArray?): String? {
        if (data == null) {
            return null
        } else {
            val str = StringBuilder()
            for (aData in data) {
                if (aData and 0xFF.toByte() < 16)
                    str.append("0").append(Integer.toHexString((aData and 0xFF.toByte()).toInt()))
                else
                    str.append(Integer.toHexString((aData and 0xFF.toByte()).toInt()))
            }
            return str.toString().toUpperCase()
        }
    }

    @Throws(Exception::class)
    private fun init() {
        val keygenerator = KeyGenerator.getInstance(AppHelper.Const.ENCRYPTION_ALGORITHM_NAME)
        val secretkey = keygenerator.generateKey()

        key = SecretKeySpec(secretkey.encoded, AppHelper.Const.ENCRYPTION_ALGORITHM_NAME)
        cipher = Cipher.getInstance(AppHelper.Const.ENCRYPTION_ALGORITHM_NAME)
    }

    @Throws(InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun encryptText(message: String): String? {
        cipher!!.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher!!.doFinal(message.toByteArray())

        return bytesToHex(encrypted)
    }

    @Throws(InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun decryptText(encryptedString: String): String? {
        cipher!!.init(Cipher.DECRYPT_MODE, key)
        val decrypted = cipher!!.doFinal(hexToBytes(encryptedString))

        return bytesToHex(decrypted)
    }

}
