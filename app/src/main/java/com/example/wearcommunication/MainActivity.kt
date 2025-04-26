package com.example.wearcommunication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable

class MainActivity : AppCompatActivity(), DataClient.OnDataChangedListener {

    private lateinit var messageTextView: TextView
    private lateinit var statusTextView: TextView
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val databaseManager by lazy { DatabaseManager.getInstance(this) }

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "phone-message-received") {
                val message = intent.getStringExtra("message") ?: "No message"
                updateUI("Mensagem recebida do smartwatch", message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageTextView = findViewById(R.id.message_text)
        statusTextView = findViewById(R.id.status_text)

        // Registra o BroadcastReceiver
        registerReceiver(messageReceiver, IntentFilter("phone-message-received"))
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(messageReceiver)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                val uri = event.dataItem.uri
                if (uri.path == "/data-path") {
                    val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                    val message = dataMap.getString("message") ?: "No data"

                    // Atualiza a UI
                    runOnUiThread {
                        updateUI("Dados recebidos do smartwatch", message)
                    }

                    // Salva no banco de dados
                    databaseManager.saveMessageToDatabase(message)
                }
            }
        }
    }

    private fun updateUI(status: String, message: String) {
        statusTextView.text = status
        messageTextView.text = message
    }
}