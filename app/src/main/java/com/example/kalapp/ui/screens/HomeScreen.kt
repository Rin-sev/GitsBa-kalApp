package com.example.kalapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalapp.R
import com.example.kalapp.ui.theme.*


@Composable
fun HomeScreen(
    onSenderClick : ()  -> Unit,
    onReceiverClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // +--- top: app identity ---+ //
        Column(
            modifier            = Modifier.padding(top = 80.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image (
                painter = painterResource( id = R.drawable.kalapp_logo ),
                contentDescription = "kalApp logo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
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

        // --- middle : role selection --- //
        Column (
            modifier            = Modifier.padding( horizontal = 24.dp ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text (
                text       = "SELECT YOUR ROLE",
                fontSize   = 13.sp,
                color      = TextMuted,
                fontWeight = FontWeight.Medium
            )

            // +--- ROLE BUTTONS ---+ //
            // Sender button
            RoleButton (
                label       = "Sender",
                description = "Report household status",
                color       = UrgentRed,
                onClick     = onSenderClick
            )

            // Receiver button
            RoleButton (
                label       = "Receiver",
                description = "Monitor incoming reports",
                color       = SafeGreen,
                onClick     = onReceiverClick
            )
        }

        // +--- bottom : footer ---+ //
        Text (
            text      = "<footer message here>>",
            fontSize  = 11.sp,
            color     = TextSecondary,
            modifier  = Modifier.padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )
    }
}

// +----- ROLE BUTTON -----+ //

@Composable
fun RoleButton (
    label : String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Button (
        onClick  = onClick,
        colors   = ButtonDefaults.buttonColors(containerColor = color),
        shape    = RoundedCornerShape( 14.dp ),
        modifier = Modifier
            .fillMaxWidth()
            .height( 80.dp )
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text (
                text       = label,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.White
            )

            Text (
                text = description,
                fontSize = 12.sp,
                color = Color.White.copy( alpha = 0.75f )
            )
        }
    }
}