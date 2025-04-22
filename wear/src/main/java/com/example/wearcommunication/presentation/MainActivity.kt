package com.example.wearcommunication

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

class MainActivity : ComponentActivity() {

    private lateinit var sendButton: Button
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val nodeClient by lazy { Wearable.getNodeClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton = findViewById(R.id.send_button)
        sendButton.setOnClickListener {
            sendMessageToPhone()
        }
    }

    private fun sendMessageToPhone() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Busca os nós conectados
                val nodes = withContext(Dispatchers.IO) {
                    nodeClient.connectedNodes.await()
                }

                // Envia mensagem para cada nó
                for (node in nodes) {
                    val timestamp = Date().time.toString()
                    val message = "Dados do Smartwatch: $timestamp"

                    withContext(Dispatchers.IO) {
                        messageClient.sendMessage(node.id, "/message-path", message.toByteArray())
                            .await()
                    }

                    Toast.makeText(
                        this@MainActivity,
                        "Mensagem enviada: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Erro ao enviar mensagem: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Método alternativo usando DataLayer (mais confiável para dados que precisam persistir)
    private fun sendDataToPhone() {
        val dataRequest = PutDataMapRequest.create("/data-path").apply {
            dataMap.putString("message", "Dados do Smartwatch: ${Date().time}")
            dataMap.putLong("timestamp", Date().time)
        }.asPutDataRequest()

        dataRequest.setUrgent()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    dataClient.putDataItem(dataRequest).await()
                }
                Toast.makeText(this@MainActivity, "Dados enviados com sucesso", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro ao enviar dados: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}