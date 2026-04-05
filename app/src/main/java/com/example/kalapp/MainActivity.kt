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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalAppTheme {
                Scaffold { innerPadding ->

                    Column (
                        modifier = Modifier
                            .padding ( innerPadding )
                            .padding( 16.dp ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text( text = "Hello, World!" )
                        Text( text = "Gits Ba was here!" )
                    }

                }
            }
        }
    }
}