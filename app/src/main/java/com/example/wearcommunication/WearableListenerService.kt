package com.example.wearcommunication

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class PhoneMessageService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/message-path") {
            val message = String(messageEvent.data)

            // Envia um broadcast para atualizar a UI
            val intent = Intent("phone-message-received")
            intent.putExtra("message", message)
            sendBroadcast(intent)

            // Salva a mensagem no banco de dados
            DatabaseManager.getInstance(applicationContext).saveMessageToDatabase(message)
        }
    }
}