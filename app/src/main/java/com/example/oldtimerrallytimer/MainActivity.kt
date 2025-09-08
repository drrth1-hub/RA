package com.example.oldtimerrallytimer

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oldtimerrallytimer.ui.*
import com.example.oldtimerrallytimer.vm.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val vm: AppViewModel = viewModel()
                val nav = rememberNavController()

                val permissions = listOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
                )
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { }
                )
                LaunchedEffect(Unit) { launcher.launch(permissions.toTypedArray()) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            listOf("Training", "Wettbewerb", "Vorgaben", "Einstellungen").forEach { route ->
                                NavigationBarItem(
                                    selected = vm.currentRoute == route,
                                    onClick = { vm.currentRoute = route; nav.navigate(route) },
                                    label = { Text(route) },
                                    icon = { Icon(Icons.Default.Info, contentDescription = null) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    AppNavHost(nav, vm, Modifier.padding(padding))
                }
            }
        }
    }
}