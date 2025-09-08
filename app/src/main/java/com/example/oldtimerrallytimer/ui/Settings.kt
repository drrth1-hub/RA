package com.example.oldtimerrallytimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oldtimerrallytimer.vm.AppViewModel
import com.example.oldtimerrallytimer.model.ConnectionType

@Composable
fun SettingsScreen(vm: AppViewModel) {
    val ui by vm.uiState.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Einstellungen", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Verbindungsart")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ConnectionType.values().forEach { ct ->
                FilterChip(
                    selected = ui.connectionType == ct,
                    onClick = { vm.setConnectionType(ct) },
                    label = { Text(ct.display) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.toggleTtsMode() }) { Text("Ansage: " + if (ui.countdownMode) "Countdown" else "Countup") }
            Button(onClick = { vm.togglePrecount() }) { Text("Pre-Countdown: ${ui.precountSeconds}s") }
        }
        Spacer(Modifier.height(16.dp))
        AssistChip(onClick = { vm.toggleSimulator() }, label = { Text(if (ui.simulator) "Simulator AN" else "Simulator AUS") })
    }
}