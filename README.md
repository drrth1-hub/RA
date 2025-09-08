# Oldtimer Rally Timer (Demo Full Project)

Vollständiges Android Studio Projekt (Kotlin + Jetpack Compose). Es erfüllt die Kernpunkte des Pflichtenhefts:
- Training & Wettbewerb mit zwei Ansichten
- 1/100s Zeitstempel, laufende Tabellenanzeige und Countdown/Ansage (TTS)
- Verbindungsart (USB/BT/WIFI) wählbar und grüne „Verbunden“-Leuchte (Simulatormodus integriert)
- CSV Import/Export-Schnittstellen und Dateinamenkonventionen vorbereitet
- Foreground Service für Signale (>= 2s), Hintergrundupdates

> Hinweis: Ohne reale ESP32-Verbindung läuft ein integrierter Simulator, damit alles sofort testbar ist. Die Schnittstellen sind so gekapselt, dass echte Verbindungen leicht nachrüstbar sind.

## Start
1. Zip entpacken und mit **Android Studio 2025.1.2** öffnen.
2. Sync/Build ausführen und App auf ein Gerät (minSdk 26) installieren.
3. In „Einstellungen“ ggf. **Simulator AN** lassen, „Training“ starten.

## CSV-Formate
- Training Vorgaben: `RD|A→B|SOLL` (RD=0 für alle Runden)
- Wettbewerb Vorgaben: `TP|A→B|SOLL`

Ergebnis-Export & Dateinamen (z.B. `ERG_JJ-MM-TT-HH-MIN.csv`) sind vorbereitet (siehe `AppViewModel.exportTrainingCsv()`).

## Struktur
- `app/src/main/java/.../ui` Compose Screens
- `model` Datenklassen
- `service/SignalService` Simulator + Foreground Service
- `vm/AppViewModel` Businesslogik, TTS, Tabellen, Countdown