package com.example.oldtimerrallytimer.vm

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.oldtimerrallytimer.model.*
import com.example.oldtimerrallytimer.service.SignalService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class AppViewModel(app: Application): AndroidViewModel(app) {
    private val _ui = MutableStateFlow(UiState())
    val uiState = _ui.asStateFlow()
    var currentRoute: String
        get() = _ui.value.currentRoute
        set(value) { _ui.value = _ui.value.copy(currentRoute = value) }

    private var tts: TextToSpeech? = TextToSpeech(app) { }

    private val vorgaben = mutableListOf<Vorgabe>()
    private val trainingSignals = mutableListOf<Signal>()
    private val compSignals = mutableListOf<Signal>()

    init {
        // preload some sample presets
        vorgaben.addAll(listOf(
            Vorgabe(0,1,2, 300), // 3.00s
            Vorgabe(0,2,3, 250), // 2.50s
            Vorgabe(0,3,4, 400)  // 4.00s
        ))
        updatePresetPreview()
    }

    fun setConnectionType(ct: ConnectionType) { _ui.value = _ui.value.copy(connectionType = ct) }
    fun toggleSimulator() { _ui.value = _ui.value.copy(simulator = !_ui.value.simulator) }
    fun toggleTtsMode() { _ui.value = _ui.value.copy(countdownMode = !_ui.value.countdownMode) }
    fun togglePrecount() {
        val s = _ui.value.precountSeconds
        _ui.value = _ui.value.copy(precountSeconds = if (s >= 10) 3 else s + 1)
    }

    fun switchTrainingView() { _ui.value = _ui.value.copy(trainingView = if (_ui.value.trainingView == 1) 2 else 1) }
    fun switchCompetitionView() { _ui.value = _ui.value.copy(competitionView = if (_ui.value.competitionView == 1) 2 else 1) }

    fun toggleTraining() {
        val nowActive = !_ui.value.trainingActive
        _ui.value = _ui.value.copy(trainingActive = nowActive)
        if (nowActive) startSignalFlow(Mode.TRAINING) else stopSignalFlow()
    }
    fun toggleCompetition() {
        val nowActive = !_ui.value.competitionActive
        _ui.value = _ui.value.copy(competitionActive = nowActive)
        if (nowActive) startSignalFlow(Mode.WETTBEWERB) else stopSignalFlow()
    }

    private fun startSignalFlow(mode: Mode) {
        viewModelScope.launch {
            SignalService.start(getApplication(), mode, _ui.value.simulator) { sig ->
                onSignal(sig, mode)
            }
            _ui.value = _ui.value.copy(connected = true)
        }
    }
    private fun stopSignalFlow() {
        SignalService.stop(getApplication())
        _ui.value = _ui.value.copy(connected = false)
    }

    private fun onSignal(sig: Signal, mode: Mode) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.getDefault())
        val stamp = sdf.format(Date(sig.timestampMs))

        when (mode) {
            Mode.TRAINING -> {
                trainingSignals.add(sig)
                computeRows(trainingSignals, true)
            }
            Mode.WETTBEWERB -> {
                compSignals.add(sig)
                computeRows(compSignals, false)
            }
        }
        // simplistic countdown announcement (every signal announces next target seconds)
        val next = nextSollSeconds(index = (if (mode==Mode.TRAINING) trainingSignals.size else compSignals.size))
        if (next != null) {
            val text = if (_ui.value.countdownMode) "noch ${next.second}s" else "${next.second}s"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
            _ui.value = _ui.value.copy(
                currentCountdownLine = "A→B ${next.first}: ${next.second}s",
                nextCountdownLines = listOf()
            )
        }
    }

    private fun nextSollSeconds(index: Int): Pair<Int, Int>? {
        val v = vorgaben.getOrNull((index)%vorgaben.size) ?: return null
        return Pair(v.b, v.sollHundredths/100)
    }

    private fun computeRows(signals: List<Signal>, training: Boolean) {
        val rows = mutableListOf<String>()
        var sumAbs = 0.0
        for (i in 1 until signals.size) {
            val dtMs = signals[i].timestampMs - signals[i-1].timestampMs
            val ist = dtMs/1000.0
            val vorgabe = vorgaben.getOrNull((i-1)%vorgaben.size)?.sollHundredths ?: 0
            val soll = vorgabe/100.0
            val abw = ist - soll
            sumAbs += abs(abw)
            rows.add("${i} | ${i}→${i+1} | %1$.2f | %2$.2f | %3$.2f".format(ist, soll, abw))
        }
        rows.add("Σ |     |       |       | %1$.2f".format(sumAbs))
        if (training) _ui.value = _ui.value.copy(trainingRows = rows) else _ui.value = _ui.value.copy(competitionRows = rows)
    }

    fun importCsv(isTraining: Boolean) {
        // For simplicity inside this demo, load from assets (real app: SAF picker). This keeps project self-contained.
        viewModelScope.launch {
            vorgaben.clear()
            vorgaben.addAll(listOf(
                Vorgabe(0,1,2,300),
                Vorgabe(0,2,3,250),
                Vorgabe(0,3,4,400)
            ))
            updatePresetPreview()
        }
    }

    fun exportVorgabenCsv() {
        // No-op demo: would write to app-specific storage with names VG_JJ-MM-TT-XX.csv or TP_XX
        updatePresetPreview()
    }

    fun exportTrainingCsv() {
        // No-op demo: left as exercise to use SAF – structure retained
    }

    private fun updatePresetPreview() {
        _ui.value = _ui.value.copy(presetPreview = vorgaben.take(12).map {
            "RD/TP ${it.rdOrTp} | ${it.a}→${it.b} | SOLL ${it.sollHundredths/100.0}s"
        })
    }
}