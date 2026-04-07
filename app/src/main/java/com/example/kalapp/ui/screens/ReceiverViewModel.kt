package com.example.kalapp.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalapp.KalAppDatabase
import com.example.kalapp.SmsMessageBus
import com.example.kalapp.model.TriageMessage
import com.example.kalapp.model.TriageStatus
import com.example.kalapp.model.toDomain
import com.example.kalapp.model.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Counters from the message log
data class TriageCounters(
    val unknown: Int = 0,
    val urgent: Int = 0,
    val evacuated: Int = 0,
    val safe: Int = 0,
    val dispatched: Int = 0   // acknowledged == true
)

class ReceiverViewModel (application: Application) : AndroidViewModel(application) {
    // --- Message log (newest first) ---

    private val dao = KalAppDatabase.getInstance(application).triageMessageDao()
    private val _messages = MutableStateFlow<List<TriageMessage>>(emptyList())
    val messages: StateFlow<List<TriageMessage>> = _messages.asStateFlow()

    // --- Derived counters ---
    private val _counters = MutableStateFlow(TriageCounters())
    val counters: StateFlow<TriageCounters> = _counters.asStateFlow()

    //---------------------------------------------------------------------------
    // Called by SMS BroadcastReceiver (stub) when a message arrives
    // TODO: wire up SmsReceiver BroadcastReceiver here
    //---------------------------------------------------------------------------

    init {
        // Observe Room database - auto-updates when new SMS arrives
        viewModelScope.launch {
            dao.observeAll().collect { entities ->
                val domainList = entities.map { it.toDomain() }
                _messages.value = domainList
                recomputeCounters(domainList)
            }
        }

        // Also collect from bus if app is open
        viewModelScope.launch {
            SmsMessageBus.incoming.collect { message ->
                dao.insert(message.toEntity())
            }
        }
    }

        //---------------------------------------------------------------------------
        // Acknowledge / Respond to a household - flips acknowledged = true
        //---------------------------------------------------------------------------

        fun acknowledge(messageId: String) {
            viewModelScope.launch {
                dao.acknowledge(messageId)
            }
        }


    //---------------------------------------------------------------------------
    // Counters
    // Unknown = households not yet seen (placeholder is 0 until we wire in the
    //           full household roster)
    // dispatched = acknowledged == true (regardless of status)
    //---------------------------------------------------------------------------

    private fun recomputeCounters(list: List<TriageMessage>) {
        _counters.update {
            TriageCounters(
                unknown = 0,    // TODO: derive from sample households minus received
                urgent = list.count { !it.acknowledged && it.status == TriageStatus.URGENT },
                evacuated = list.count { !it.acknowledged && it.status == TriageStatus.EVACUATED },
                safe = list.count { !it.acknowledged && it.status == TriageStatus.SAFE },
                dispatched = list.count { !it.acknowledged }
            )
        }
    }

    //---------------------------------------------------------------------------
    // Dev / demo helper - inject a fake message to test the UI
    // Note to remove before production
    //---------------------------------------------------------------------------

    fun injectSample(message: TriageMessage) {
        viewModelScope.launch {
            dao.insert(message.toEntity())
        }
    }
}