package com.example.oldtimerrallytimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oldtimerrallytimer.vm.AppViewModel

@Composable
fun PresetsScreen(vm: AppViewModel) {
    val ui by vm.uiState.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vorgaben", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.importCsv(isTraining = true) }) { Text("VG Training import (CSV)") }
            Button(onClick = { vm.importCsv(isTraining = false) }) { Text("TP Wettbewerb import (CSV)") }
            Button(onClick = { vm.exportVorgabenCsv() }) { Text("VG speichern") }
        }
        Spacer(Modifier.height(12.dp))
        Text("CSV Format Training: RD|A→B|SOLL; RD=0 gilt für alle Runden")
        Text("CSV Format Wettbewerb: TP|A→B|SOLL")
        Spacer(Modifier.height(8.dp))
        Text("Aktuelle Vorgaben:")
        ui.presetPreview.take(10).forEach { Text(it) }
    }
}