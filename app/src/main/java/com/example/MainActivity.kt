package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ParentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup edge-to-edge full screen bleeds
        enableEdgeToEdge()
        
        // Single instance offline-model view model
        val viewModel = ParentViewModel(application)

        setContent {
            MyApplicationTheme {
                // State-driven routing engine: "login", "dashboard", "sleep", "crying", "meals", "vaccines", "milestones", "timeline"
                var activeRoute by remember { mutableStateOf("login") }
                
                val currentUser by viewModel.currentUser.collectAsState()

                // Dynamic routing locks to force login page if user isn't authenticated
                LaunchedEffect(currentUser) {
                    if (currentUser == null) {
                        activeRoute = "login"
                    } else if (activeRoute == "login") {
                        activeRoute = "dashboard"
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Crossfade(
                        targetState = activeRoute,
                        animationSpec = tween(durationMillis = 250),
                        label = "MainCrossfadeNavigation"
                    ) { route ->
                        when (route) {
                            "login" -> {
                                LoginScreen(
                                    viewModel = viewModel,
                                    onLoginSuccess = { activeRoute = "dashboard" }
                                )
                            }
                            "dashboard" -> {
                                DashboardScreen(
                                    viewModel = viewModel,
                                    onNavigate = { destination -> activeRoute = destination },
                                    onLogout = {
                                        viewModel.logout()
                                        activeRoute = "login"
                                    }
                                )
                            }
                            "sleep" -> {
                                SleepScreen(
                                    viewModel = viewModel,
                                    onBack = { activeRoute = "dashboard" }
                                )
                            }
                            "crying" -> {
                                CryingScreen(
                                    viewModel = viewModel,
                                    onBack = { activeRoute = "dashboard" }
                                )
                            }
                            "meals" -> {
                                MealsScreen(
                                    viewModel = viewModel,
                                    onBack = { activeRoute = "dashboard" }
                                )
                            }
                            "vaccines" -> {
                                VaccineScreen(
                                    viewModel = viewModel,
                                    onBack = { activeRoute = "dashboard" }
                                )
                            }
                            "milestones" -> {
                                MilestonesScreen(
                                    viewModel = viewModel,
                                    onBack = { activeRoute = "dashboard" }
                                )
                            }
                            "timeline" -> {
                                TimelineScreen(
                                    viewModel = viewModel,
                                    onBack = { activeRoute = "dashboard" }
                                )
                            }
                            else -> {
                                DashboardScreen(
                                    viewModel = viewModel,
                                    onNavigate = { destination -> activeRoute = destination },
                                    onLogout = {
                                        viewModel.logout()
                                        activeRoute = "login"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
