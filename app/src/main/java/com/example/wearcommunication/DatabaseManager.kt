package com.example.wearcommunication

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class DatabaseManager private constructor(private val context: Context) {
    companion object {
        private const val TAG = "DatabaseManager"
        private const val PHP_SCRIPT_URL = "http://10.0.2.2/wear_app/save_message.php"
        
        @Volatile
        private var instance: DatabaseManager? = null
        
        fun getInstance(context: Context): DatabaseManager {
            return instance ?: synchronized(this) {
                instance ?: DatabaseManager(context.applicationContext).also { instance = it }
            }
        }
    }
    
    fun saveMessageToDatabase(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Preparar os parâmetros para enviar ao PHP
                val postData = "message=${URLEncoder.encode(message, "UTF-8")}"
                
                val url = URL(PHP_SCRIPT_URL)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                conn.setRequestProperty("Content-Length", postData.length.toString())
                
                conn.outputStream.use { os ->
                    val input = postData.toByteArray(charset("UTF-8"))
                    os.write(input, 0, input.size)
                }
                
                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    conn.inputStream.bufferedReader().use { reader ->
                        val response = reader.readText()
                        Log.d(TAG, "Server response: $response")
                    }
                } else {
                    Log.e(TAG, "HTTP error code: $responseCode")
                }
                
                conn.disconnect()
            } catch (e: Exception) {
                Log.e(TAG, "Error saving to database: ${e.message}", e)
            }
        }
    }
}