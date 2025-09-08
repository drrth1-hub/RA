package com.example.oldtimerrallytimer.model

import kotlinx.serialization.Serializable

enum class Mode { TRAINING, WETTBEWERB }

enum class ConnectionType(val display: String) { USB("USB"), BT("BT"), WIFI("WIFI") }

@Serializable
data class Vorgabe(val rdOrTp: Int, val a: Int, val b: Int, val sollHundredths: Int)

@Serializable
data class Signal(val timestampMs: Long, val point: Int)

data class UiState(
    val connected: Boolean = false,
    val simulator: Boolean = true,
    val connectionType: ConnectionType = ConnectionType.WIFI,
    val trainingActive: Boolean = false,
    val competitionActive: Boolean = false,
    val trainingView: Int = 1,
    val competitionView: Int = 1,
    val presetPreview: List<String> = emptyList(),
    val trainingRows: List<String> = emptyList(),
    val competitionRows: List<String> = emptyList(),
    val currentCountdownLine: String = "--",
    val nextCountdownLines: List<String> = emptyList(),
    val countdownMode: Boolean = true,
    val precountSeconds: Int = 3,
    var currentRoute: String = "Training"
)