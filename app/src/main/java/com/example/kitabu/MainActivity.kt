package com.example.kitabu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kitabu.ui.navigation.KitabuNavGraph
import com.example.kitabu.ui.theme.KitabuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitabuTheme {
                KitabuNavGraph()
            }
        }
    }
}
