package com.example.kalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kalapp.ui.theme.KalAppTheme

// ADDED AS PER CLAUDE
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.kalapp.ui.screens.HomeScreen

// ADDED AS PER GOOGLE AI
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalAppTheme {
                KalAppNavigation()
            }
        }
    }
}


@Composable
fun KalAppNavigation() {
    val navController = rememberNavController()

    NavHost (
        navController    = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onSenderClick   = { navController.navigate("sender") },
                onReceiverClick = { navController.navigate("receiver") }
            )
        }

        // placeholder routes for now
        composable("sender") {

        }

        composable("receiver") {

        }
    }
}