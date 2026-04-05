package com.example.kalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalapp.ui.theme.*


@Composable
fun HomeScreen(
    onSenderClick : ()  -> Unit,
    onReceiverClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // --- top: app identity ---
        Column(
            modifier            = Modifier.padding(top = 80.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text (
                text = "kalApp",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer ( modifier = Modifier.height(8.dp) )

            Text (
                text      = "Gits Ba Triage",
                fontSize  = 14.sp,
                color     = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer ( modifier = Modifier.height(4.dp) )

            Text (
                text      = "Barangay-level household triage system",
                fontSize  = 12.sp,
                color     = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}