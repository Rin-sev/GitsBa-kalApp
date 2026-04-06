package com.example.kalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kalapp.model.Household
import com.example.kalapp.model.TriageMessage
import com.example.kalapp.model.TriageStatus
import com.example.kalapp.ui.theme.*

// sender screen root
//mirrors: #sender-panel > #sender-screen > #upper-screen + #lower-screen + #house-id-div
@Composable
fun SenderScreen(viewModel: SenderViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    //mirrors: .panel-emulation - aliceblue background, column layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F8FF)) //aliceblue
            .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)){

        //panel label - mirrors: <h2>Sender Panel</h2>
        Text(
            text = "Sender Panel",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary)

        //sender-screen - mirrors: #upper-screen border + app name + buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp,Color.Gray)
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally){

            //mirrors: #app-name - replace with image() when logo is ready
            Text(
                text = "kalApp",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp)
            )

            //buttons-div - mirrors: #buttons-div column of three buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally){

                //mirrors: #urgent-button
                StatusButton(
                    label = "URGENT",
                    bgColor = Color(0xFFCC222), //color-immediate-main
                    borderColor = Color(0xFF8B0000), //color-immediate-border
                    textColor = Color.White,
                    enabled = uiState.selectedHousehold != null,
                    onClick = {viewModel.sendStatus(TriageStatus.URGENT)}
                )

                //mirrors: #evacuated-button
                StatusButton(
                    label = "EVACUATED",
                    bgColor = Color(0xFFD4A800), //color-evacuated-main
                    borderColor = Color(0xFFB38600), //color-evacuated-border
                    textColor = Color.Black,
                    enabled = uiState.selectedHousehold != null,
                    onClick = {viewModel.sendStatus(TriageStatus.EVACUATED)}
                )

                //mirror: #safe-button
                StatusButton(
                    label = "SAFE",
                    bgColor = Color(0xFF27AE60), //color-safe-main
                    borderColor = Color(0xFF1E8449), //color-safe-border
                    textColor = Color.White,
                    enabled = uiState.selectedHousehold != null,
                    onClick = {viewModel.sendStatus(TriageStatus.SAFE)}
                )
            }

        }

    }


    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text (
            text = "Hello, World! You are in the Sender Screen.",
            fontSize = 24.sp
        )
    }
}


@Composable
fun StatusButton (
    label : String,
    bgColor : Color,
    borderColor : Color,
    textColor : Color,
    enabled : Boolean,
    onClick : () -> Unit
) {
    Button (
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors (
            containerColor = bgColor,
            disabledContainerColor = bgColor.copy ( alpha = 0.35f )
        ),
        shape = RoundedCornerShape (5.dp),
        modifier = Modifier
            .width ( 225.dp )
            .height ( 65.dp )
    ) {
        Text (
            text = label,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) textColor else textColor.copy (alpha = 0.5f)
        )
    }
}