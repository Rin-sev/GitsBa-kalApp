package com.example.kalapp.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

//sent log table
//mirrors: #scrollable + #entry-table with thead(Date, Time, Status, House id)

//sent log row
//mirrors: #entry-table th/td - text-align left, padding 6px, border-bottom

@Composable
fun SentLogRow(
    date: String,
    time: String,
    status: String,
    houseId: String,
    isHeader: Boolean = false,
    statusColor: Color = TextPrimary
){
    val fontSize = if(isHeader) 10.sp else 11.sp
    val fontWeight = if(isHeader) FontWeight.Bold else FontWeight.Normal
    val color = if(isHeader) TextSecondary else TextPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 2.dp)
    ){
        Text(date, fontSize = fontSize, fontWeight = fontWeight, color = color, modifier = Modifier.weight(1.8f))
        Text(time,fontSize = fontSize, fontWeight = fontWeight, color = color, modifier = Modifier.weight(2f))
        Text(
            text = status,
            fontSize = fontSize,
            fontWeight = if(isHeader) fontWeight else FontWeight.Bold,
            color = if(isHeader) color else statusColor,
            modifier = Modifier.weight(2f)
        )
        Text(houseId, fontSize=fontSize, fontWeight=fontWeight, color=color, modifier=Modifier.weight(1.8f))
    }
}
@Composable
fun SentLogTable(entries: List<TriageMessage>){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .padding(5.dp)
    ){
        //Table header - mirrors: .entry-head <tr>
        SentLogRow(
            date = "Date",
            time = "Time",
            status = "Status",
            houseId = "House ID",
            isHeader = true
        )
        HorizontalDivider(color = Color(0xFFCCCCCC))

        //scrollable body - mirrors: #scrollable height 105dp overflow-y auto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 105.dp)
        ){
            if(entries.isEmpty()){
                Text(
                    text = "No entries yet.",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            } else{
                LazyColumn{
                    items(entries){message->
                        SentLogRow(
                            date = message.formattedDate,
                            time = message.formattedTime,
                            status = message.status.label,
                            houseId = message.houseId,
                            statusColor = when(message.status){
                                TriageStatus.URGENT -> Color(0xFFCC2222)
                                TriageStatus.EVACUATED -> Color(0xFFD4A800)
                                TriageStatus.SAFE -> Color (0xFF27AE60)
                            }
                        )
                        HorizontalDivider(color = Color(0xFFCCCCCC))
                    }
                }
            }
        }
    }
}

// sender screen root
//mirrors: #sender-panel > #sender-screen > #upper-screen + #lower-screen + #house-id-div
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SenderScreen(viewModel: SenderViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    //SMS permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted->
        viewModel.onSmsPermssionResult(isGranted)
    }

    LaunchedEffect(Unit){
        permissionLauncher.launch(Manifest.permission.SEND_SMS)
    }

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
                    bgColor = Color(0xFFCC2222), //color-immediate-main
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

                //hint shown when no household is selected yet
                if (uiState.selectedHousehold == null) {
                    Text(
                        text = "Select a household below to enable sending.",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        //household dropdown - mirrors:house-id-div
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .padding(5.dp)
        ){
            Text(
                text = "Select Household",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            ExposedDropdownMenuBox(
                expanded = uiState.isDropdownExpanded,
                onExpandedChange = { viewModel.onDropdownToggle()}
            ) {
                OutlinedTextField(
                    value = uiState.selectedHousehold?.let {
                        "${it.id} - ${it.name}"
                    } ?: "--Select a household --",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 12.sp, color = Color.Black)
                )

                ExposedDropdownMenu(
                    expanded = uiState.isDropdownExpanded,
                    onDismissRequest = { viewModel.onDropdownDismiss()}
                ) {
                    uiState.households.forEach { household ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${household.id} - ${household.name}",
                                    fontSize = 12.sp
                                )
                            },
                            onClick = { viewModel.onHouseholdSelected(household)}
                        )
                    }
                }
            }
        }

        //SMS result feedback
        uiState.lastSmsResult?.let{result ->
            Text(
                text = result,
                fontSize = 10.sp,
                color = if(result.startsWith("SMS sent")) Color(0xFF27AE60) else Color.Red,
                modifier = Modifier.padding(top=4.dp)
            )
        }
            //lower screen - mirrors: #lower-screen +#scrollable +#entry-table
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                SentLogTable(entries = uiState.sentLog)
            }
        }

        }

//status button
// mirrors: #button-div button - width 225dp, height 65dp, bold 25sp, groove border

@Composable
fun StatusButton(
    label: String,
    bgColor: Color,
    borderColor: Color,
    textColor: Color,
    enabled: Boolean,
    onClick: ()-> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            disabledContainerColor = bgColor.copy(alpha = 0.35f)
        ),
        shape = RoundedCornerShape(5.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(borderColor)
        ),
        modifier = Modifier
            .width(225.dp)
            .height(65.dp)
    ) {
        Text(
            text = label,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) textColor else textColor.copy(alpha = 0.5f)
        )
    }
}