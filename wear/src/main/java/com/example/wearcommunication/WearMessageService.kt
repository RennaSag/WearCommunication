package com.example.wearcommunication

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class WearMessageService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/message-path") {
            val message = String(messageEvent.data)

            // envia um broadcast para atualizar a UI
            val intent = Intent("wear-message-received")
            intent.putExtra("message", message)
            sendBroadcast(intent)
        }
    }
}