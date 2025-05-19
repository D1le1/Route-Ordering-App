import com.example.operatordesktop.controllers.LoginController
import com.example.operatordesktop.util.ServerWork
import kotlinx.coroutines.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Tests {
    @Before
    fun connect() = runBlocking {
        ServerWork.connectToServer()
        delay(100)
        val response =
            ServerWork.sendRequest("AUTH--111--${hashPassword("111")}")
        assertNotNull(response)
    }

    @Test
    fun auth() = runBlocking {
        ServerWork.connectToServer()
        delay(100)
        val response =
                ServerWork.sendRequest("AUTH--111--${hashPassword("111")}")

        println(response)
        assertNotNull(response)
    }

    @Test
    fun search() = runBlocking {
        ServerWork.connectToServer()
        delay(100)
        val response =
                ServerWork.sendRequest("SEARCH--Минск--Бобруйск--25.05.2024")

        println(response)
        assertTrue(!response.equals("[]"))
    }

    private fun hashPassword(password: String): String? {
        return if (password != "") {
            try {
                // Получаем экземпляр класса MessageDigest с алгоритмом SHA-256
                val messageDigest = MessageDigest.getInstance("SHA-256")

                // Преобразуем пароль в байтовый массив
                val passwordBytes = password.toByteArray()

                // Вычисляем хэш-код пароля
                val hashBytes = messageDigest.digest(passwordBytes)

                // Преобразуем хэш-код в шестнадцатеричную строку
                val stringBuilder = StringBuilder()
                for (b in hashBytes) {
                    stringBuilder.append(String.format("%02x", b))
                }
                stringBuilder.toString()
            } catch (e: NoSuchAlgorithmException) {
                // Обработка исключения
                e.printStackTrace()
                null
            }
        } else {
            " "
        }
    }
}