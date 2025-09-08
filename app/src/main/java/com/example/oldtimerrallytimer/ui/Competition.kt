package com.example.oldtimerrallytimer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oldtimerrallytimer.vm.AppViewModel

@Composable
fun CompetitionScreen(vm: AppViewModel) {
    val ui by vm.uiState.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Wettbewerb", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.toggleCompetition() }) {
                Text(if (ui.competitionActive) "Stop" else "Start")
            }
            Button(onClick = { vm.switchCompetitionView() }) {
                Text("Ansicht wechseln")
            }
        }
        Spacer(Modifier.height(8.dp))
        if (ui.competitionView == 1) {
            LazyColumn {
                item {
                    Text("IST/SOLL Tabelle")
                }
                items(ui.competitionRows) { row -> Text(row) }
            }
        } else {
            Column {
                Text("RD/TP | A→B | ΔSOLL | Restzeit (s)", style = MaterialTheme.typography.titleMedium)
                Text(ui.currentCountdownLine, style = MaterialTheme.typography.displaySmall)
                Spacer(Modifier.height(8.dp))
                ui.nextCountdownLines.forEach { Text(it, style = MaterialTheme.typography.titleMedium) }
            }
        }
    }
}