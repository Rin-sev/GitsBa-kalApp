package com.example.kalapp

import com.example.kalapp.model.TriageMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// app-level singleton bus
// SmsReceiver posts here > ReceiverViewModel collects here
object SmsMessageBus {

    private val _incoming = MutableSharedFlow<TriageMessage>(
        extraBufferCapacity = 16
    )
    val incoming = _incoming.asSharedFlow()

    fun post(message: TriageMessage) {
        _incoming.tryEmit(message)
    }
}