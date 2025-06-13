package com.techcube.remootio

import HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.techcube.remootio.ui.theme.RemootioTheme
import com.techcube.remootio.services.Repositories
import com.techcube.remootio.ui.components.MainViewModel
import com.tomerpacific.jetpackcomposetabs.ui.view.TabLayout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: MainViewModel by viewModels()

        setContent {
            RemootioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        TabLayout(viewModel = viewModel)                    }
                }
            }
        }
    }
}





