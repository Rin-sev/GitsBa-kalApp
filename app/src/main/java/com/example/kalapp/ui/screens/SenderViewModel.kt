package com.example.kalapp.ui.screens

import androidx.lifecycle.AndroidViewModel
import com.example.kalapp.model.Household
import com.example.kalapp.model.TriageMessage
import com.example.kalapp.model.TriageStatus
import com.example.kalapp.model.sampleHouseholds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import kotlin.String
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Application
import android.telephony.SmsManager
import android.util.Log


data class SenderUiState (
    val households : List<Household> = sampleHouseholds,
    val selectedHousehold : Household? = null,
    val isDropdownExpanded : Boolean = false,
    val sentLog : List<TriageMessage> = emptyList(),
    val smsPermissionGranted: Boolean = false,
    val lastSmsResult: String? = null //feedback message for the UI
)


class SenderViewModel(application: Application) : AndroidViewModel(application) {
    private val responderNumber = "09280452230" //recipient number

    private val _uiState = MutableStateFlow(SenderUiState())
    val uiState: StateFlow<SenderUiState> = _uiState.asStateFlow()

    fun onSmsPermssionResult(isGranted: Boolean) {
        _uiState.value = _uiState.value.copy(smsPermissionGranted = isGranted)
    }

    fun onHouseholdSelected(household: Household) {
        _uiState.value = _uiState.value.copy(
            selectedHousehold = household,
            isDropdownExpanded = false
        )
    }

    fun onDropdownToggle() {
        _uiState.value = _uiState.value.copy(
            isDropdownExpanded = !_uiState.value.isDropdownExpanded
        )
    }

    fun sendStatus(status: TriageStatus) {
        val household = _uiState.value.selectedHousehold ?: return
        val message = TriageMessage(
            houseId = household.id,
            householdName = household.name,
            status = status
        )

        _uiState.value = _uiState.value.copy(
            sentLog = listOf(message) + _uiState.value.sentLog,
            lastSmsResult = null
        )

        //send SMS
        if (_uiState.value.smsPermissionGranted) {
            sendSms(
                phoneNumber = responderNumber,
                status = status,
                houseId = household.id
            )
        } else {
            _uiState.value = _uiState.value.copy(
                lastSmsResult = "SMS permission not granted. Log recorded locally only."
            )
        }
    }

    private fun sendSms(phoneNumber: String, status: TriageStatus, houseId: String) {
        val smsBody = "[kalApp] House $houseId status: ${status.label}"
        try {
            val smsManager = getApplication<Application>()
                .getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(
                phoneNumber, //recipient
                null, //service center (null = default)
                smsBody,
                null, //send PendingIntent (add later for delivery receipts)
                null //delivery PendingIntent
            )
            _uiState.value = _uiState.value.copy(
                lastSmsResult = "SMS sent to $phoneNumber"
            )
            Log.d("SenderViewModel", "SMS sent: $smsBody to $phoneNumber")
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                lastSmsResult = "SMS failed: ${e.message}"
            )
            Log.e("SenderViewModel", "SMS send failed", e)
        }
    }

    fun onDropdownDismiss() {
        _uiState.value = _uiState.value.copy(isDropdownExpanded = false)
    }

}