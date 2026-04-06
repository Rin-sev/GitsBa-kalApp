package com.example.kalapp.ui.screens

import androidx.lifecycle.ViewModel
import com.example.kalapp.model.TriageMessage
import com.example.kalapp.model.TriageStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Counters from the message log
data class TriageCounters(
    val unknown: Int = 0,
    val urgent: Int = 0,
    val evacuated: Int = 0,
    val safe: Int = 0,
    val acknowledged: Int = 0   // acknowledged == true
)

class ReceiverViewModel : ViewModel() {
    // --- Message log (newest first) ---
    private val _messages = MutableStateFlow<List<TriageMessage>>(emptyList())
    val messages: StateFlow<List<TriageMessage>> = _messages.asStateFlow()

    // --- Derived counters ---
    private val _counters = MutableStateFlow(TriageCounters())
    val counters: StateFlow<TriageCounters> = _counters.asStateFlow()

    //---------------------------------------------------------------------------
    // Called by SMS BroadcastReceiver (stub) when a message arrives
    // TODO: wire up SmsReceiver BroadcastReceiver here
    //---------------------------------------------------------------------------

    fun onMessageReceived(message: TriageMessage) {
        _messages.update { current ->
            listOf(message) + current    // newest on top
        }
        recomputeCounters()
    }

    //---------------------------------------------------------------------------
    // Acknowledge / Respond to a household - flips acknowledged = true
    //---------------------------------------------------------------------------

    fun acknowledge(messageId: String) {
        _messages.update { current ->
            current.map { msg ->
                if (msg.id == messageId) msg.copy(acknowledged = true) else msg
            }
        }
        recomputeCounters()
    }

    //---------------------------------------------------------------------------
    // Counters
    // Unknown = households not yet seen (placeholder is 0 until we wire in the
    //           full household roster)
    // dispatched = acknowledged == true (regardless of status)
    //---------------------------------------------------------------------------

    private fun recomputeCounters() {
        val list = _messages.value
        _counters.update {
            TriageCounters(
                unknown = 0,    // TODO: derive from sample households minus received
                urgent = list.count { msg -> !msg.acknowledged && msg.status == TriageStatus.URGENT },
                evacuated = list.count { msg -> msg.acknowledged && msg.status == TriageStatus.EVACUATED },
                safe = list.count { msg -> msg.acknowledged && msg.status == TriageStatus.SAFE },
                acknowledged = list.count { msg -> msg.acknowledged }
            )
        }
    }

    //---------------------------------------------------------------------------
    // Dev / demo helper - inject a fake message to test the UI
    // Note to remove before production
    //---------------------------------------------------------------------------

    fun injectSample(message: TriageMessage) = onMessageReceived(message)
}