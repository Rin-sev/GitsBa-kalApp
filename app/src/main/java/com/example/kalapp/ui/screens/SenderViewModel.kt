package com.example.kalapp.ui.screens

import androidx.lifecycle.ViewModel
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



data class SenderUiState (
    val households : List<Household> = sampleHouseholds,
    val selectedHousehold : Household? = null,
    val isDropdownExpanded : Boolean = false,
    val sentLog : List<TriageMessage> = emptyList()
)


class SenderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow( SenderUiState() )
    val uiState : StateFlow<SenderUiState> = _uiState.asStateFlow()

    fun onHouseholdSelected( household : Household ) {
        _uiState.value = _uiState.value.copy (
            selectedHousehold  = household,
            isDropdownExpanded = false
        )
    }

    fun onDropdownToggle() {
        _uiState.value = _uiState.value.copy (
            isDropdownExpanded = !_uiState.value.isDropdownExpanded
        )
    }

    fun sendStatus ( status : TriageStatus ) {
        val household = _uiState.value.selectedHousehold ?: return
        val message   = TriageMessage (
            houseId       = household.id,
            householdName = household.name,
            status        = status
        )

        _uiState.value = _uiState.value.copy (
            sentLog = listOf(message) + _uiState.value.sentLog
        )

        // SMS logic goes here

    }

    fun onDropdownDismiss() {
        _uiState.value = _uiState.value.copy(isDropdownExpanded = false)
    }

}