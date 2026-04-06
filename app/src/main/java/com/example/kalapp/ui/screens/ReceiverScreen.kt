package com.example.kalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kalapp.model.TriageMessage
import com.example.kalapp.model.TriageStatus
import com.example.kalapp.model.sampleHouseholds
import com.example.kalapp.ui.theme.*

@Composable
fun ReceiverScreen(
    viewModel: ReceiverViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val counters by viewModel.counters.collectAsState()

    // --- mirrors: #receiver-panel ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(16.dp)
    ) {

        // -- Header ---------------------------------------------------------------------
        Text(
            text = "Coordinator Dashboard",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // -- Status counters --- mirrors: #status-counters ------------------------------
        StatusCounterRow(counters = counters)

        Spacer(modifier = Modifier.height(16.dp))

        // -- Dev inject button (remove before production) -------------------------------
        SampleInjectButton(viewModel = viewModel)

        Spacer(modifier = Modifier.height(12.dp))

        // -- Update log header ----------------------------------------------------------
        Text(
            text = "Update Log",
            color = Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // -- Table header row -- mirrors: thead -----------------------------------------
        LogTableHeader()

        // -- Scrollable message rows -- mirrors: #updates-div ---------------------------
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if(messages.isEmpty()) {
                item {
                    Text(
                        text ="No reports received yet.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else {
                items(messages, key = { it.id}) { message ->
                    LogRow(
                        message = message,
                        onAcknowledge = { viewModel.acknowledge(message.id)}
                    )
                }
            }
        }
    }
}

// --- Status counter pills --------------------------------------------------------------
// mirrors: #status-counters

@Composable
private fun StatusCounterRow(counters: TriageCounters) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Row 1: UNKNOWN | URGENT | EVACUATED | SAFE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CounterPill(
                label = "UNKNOWN",
                count = counters.unknown,
                bgColor = Color(0xFF4A4A4A),
                textColor = Color.White,
                modifier = Modifier.weight(1f)
            )
            CounterPill(
                label = "URGENT",
                count = counters.urgent,
                bgColor = UrgentRed,
                textColor = Color.White,
                modifier = Modifier.weight(1f)
            )
            CounterPill(
                label = "EVACUATED",
                count = counters.evacuated,
                bgColor = EvacuatedAmber,
                textColor = Color(0xFF1A1A00),
                modifier = Modifier.weight(1f)
            )
            CounterPill(
                label = "SAFE",
                count = counters.safe,
                bgColor = SafeGreen,
                textColor = Color.White,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2: ACKNOWLEDGED (centered, half-width)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CounterPill(
                label = "ACKNOWLEDGED",
                count = counters.acknowledged,
                bgColor = DispatchedBlue,
                textColor = Color.White,
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
    }
}

@Composable
private fun CounterPill(
    label: String,
    count: Int,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.05.sp
        )
    }
}

// --- Table header -----------------------------------------------------------------------

@Composable
private fun LogTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2E))
            .padding(horizontal =8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell("Date",      Modifier.weight(2f))
        HeaderCell("Time",      Modifier.weight(2f))
        HeaderCell("HouseID",   Modifier.weight(2f))
        HeaderCell("Status",    Modifier.weight(2f))
        HeaderCell("Respond",   Modifier.weight(1.5f))
    }
}

@Composable
private fun HeaderCell(text: String, modifier: Modifier) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

// --- Individual log row --------------------------------------------------------------
// mirrors: update-row, color-coded by status

@Composable
private fun LogRow(
    message: TriageMessage,
    onAcknowledge: () -> Unit
) {
    val rowBg = when {
        message.acknowledged -> Color(0xFFF0F0F0)
        message.status == TriageStatus.URGENT    -> Color(0xFFF0B6B6)
        message.status == TriageStatus.EVACUATED -> Color(0xFFFFF2CC)
        message.status == TriageStatus.SAFE      -> Color(0xFFB6D7A8)
        else                                     -> Color.White
    }
    val statusColor = when (message.status) {
        TriageStatus.URGENT    -> Color(0xFF990000)
        TriageStatus.EVACUATED -> Color(0xFFB45F06)
        TriageStatus.SAFE      -> Color(0xFF274E13)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBg)
            .border(0.5.dp, Color(0xFF3A3A3C))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date
        Text(
            text = message.formattedDate,
            color = if (message.acknowledged) TextMuted else Color.Black,
            fontSize = 11.sp,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )
        // Time
        Text(
            text = message.formattedTime,
            color = if (message.acknowledged) TextMuted else Color.Black,
            fontSize = 11.sp,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )
        // House ID
        Text(
            text = message.houseId,
            color = if (message.acknowledged) TextMuted else Color.Black,
            fontSize = 11.sp,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )
        // Status badge
        Box(
            modifier = Modifier
                .weight(2f)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Text(
                text = message.status.label,
                color = if (message.acknowledged) TextMuted else statusColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        if (message.acknowledged) Color(0xFF2A2A2A)
                        else statusColor.copy(alpha = 0.15f),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
        // Respond button
        Box(
            modifier = Modifier.weight(1.5f),
            contentAlignment = Alignment.Center
        ) {
            if (message.acknowledged) {
                Text(
                    text = "✓",
                    color = TextMuted,
                    fontSize = 14.sp
                )
            } else {
                Button(
                    onClick = onAcknowledge,
                    colors = ButtonDefaults.buttonColors(containerColor = DispatchedBlue),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(text = "ACK", fontSize = 10.sp, color = Color.White)
                }
            }
        }
    }
}

// --- Dev inject button --------------------------------------------------------------
// Remove this composable before production

@Composable
private fun SampleInjectButton(viewModel: ReceiverViewModel) {
    val statuses = TriageStatus.entries.toTypedArray()
    var index by remember { mutableIntStateOf(0) }

    OutlinedButton(
        onClick = {
            val household = sampleHouseholds[index % sampleHouseholds.size]
            val status    = statuses[index % statuses.size]
            viewModel.injectSample(
                TriageMessage(
                    houseId          = household.id,
                    householdName    = household.name,
                    status           = status
                )
            )
            index++
        },
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("[ DEV ] Inject sample SMS report", fontSize = 12.sp)
    }
}
