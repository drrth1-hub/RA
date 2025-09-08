package com.example.oldtimerrallytimer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oldtimerrallytimer.vm.AppViewModel

@Composable
fun AppNavHost(nav: NavHostController, vm: AppViewModel, modifier: Modifier = Modifier) {
    NavHost(navController = nav, startDestination = "Training", modifier = modifier) {
        composable("Training") { TrainingScreen(vm) }
        composable("Wettbewerb") { CompetitionScreen(vm) }
        composable("Vorgaben") { PresetsScreen(vm) }
        composable("Einstellungen") { SettingsScreen(vm) }
    }
}