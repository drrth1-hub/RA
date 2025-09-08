package com.example.oldtimerrallytimer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oldtimerrallytimer.vm.AppViewModel
import com.example.oldtimerrallytimer.model.*

@Composable
fun TrainingScreen(vm: AppViewModel) {
    val ui by vm.uiState.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Training", style = MaterialTheme.typography.titleLarge)
            ConnectionStatusLight(connected = ui.connected)
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.toggleTraining() }) {
                Text(if (ui.trainingActive) "Stop" else "Start")
            }
            Button(onClick = { vm.switchTrainingView() }) {
                Text("Ansicht wechseln")
            }
            Button(onClick = { vm.exportTrainingCsv() }) {
                Text("ERG speichern")
            }
        }
        Spacer(Modifier.height(8.dp))

        if (ui.trainingView == 1) {
            // Training 1: fortlaufende Ergebnisse (scrollbar handled by LazyColumn)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Rd | A→B | ΔIST | ΔSOLL | ΔABW")
                    }
                }
                items(ui.trainingRows) { row ->
                    Text(row)
                }
            }
        } else {
            // Training 2: aktuelle Vorgaben + Restzeit
            Column(Modifier.fillMaxWidth()) {
                Text("RD/TP | A→B | ΔSOLL | Restzeit (s)", style = MaterialTheme.typography.titleMedium)
                Text(ui.currentCountdownLine, style = MaterialTheme.typography.displaySmall)
                Spacer(Modifier.height(8.dp))
                ui.nextCountdownLines.forEach { Text(it, style = MaterialTheme.typography.titleMedium) }
            }
        }
    }
}

@Composable
fun ConnectionStatusLight(connected: Boolean) {
    AssistChip(
        onClick = {},
        label = { Text(if (connected) "Verbunden" else "Getrennt") },
        leadingIcon = { Icon(if (connected) Icons.Default.CheckCircle else Icons.Default.R.string.cancel, null) }
    )
}