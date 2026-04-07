package com.example.kalapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.kalapp.model.TriageMessage
import com.example.kalapp.model.TriageStatus
import com.example.kalapp.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        for (sms in messages) {
            val body = sms.messageBody ?: continue
            Log.d("SmsReceiver", "Received SMS: $body")

            if (!body.startsWith("[kalApp]")) continue

            val parsed = parseKalAppSms(body) ?: continue

            // Save to Room database
            val db = KalAppDatabase.getInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                db.triageMessageDao().insert(parsed.toEntity())
                Log.d("SmsReceiver", "Saved to DB: ${parsed.houseId} - ${parsed.status}")
            }

            // Also post to bus in case app is open
            SmsMessageBus.post(parsed)
        }
    }

    private fun parseKalAppSms(body: String): TriageMessage? {
        return try {
            val houseIdMatch = Regex("House\\s+(\\S+)\\s+status:").find(body)
            val statusMatch  = Regex("status:\\s*(\\w+)").find(body)

            val houseId   = houseIdMatch?.groupValues?.get(1) ?: return null
            val statusRaw = statusMatch?.groupValues?.get(1) ?: return null
            val status    = TriageStatus.valueOf(statusRaw.uppercase())

            TriageMessage(
                houseId       = houseId,
                householdName = houseId,
                status        = status
            )
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Failed to parse SMS: $body", e)
            null
        }
    }
}