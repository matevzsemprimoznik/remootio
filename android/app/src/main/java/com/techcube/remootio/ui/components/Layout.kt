package com.tomerpacific.jetpackcomposetabs.ui.view

import HomeScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.outlined.Home
import com.techcube.remootio.ui.components.MainViewModel
import com.techcube.remootio.ui.screens.SettingsScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.techcube.remootio.R
import com.techcube.remootio.utils.UpdateChecker

@Composable
fun TabLayout(viewModel: MainViewModel) {
    val tabIndex = viewModel.tabIndex.observeAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.weight(1f)) {
            when (tabIndex.value) {
                0 -> HomeScreen()
                1 -> SettingsScreen()
            }
        }
        Column(modifier = Modifier.height(60.dp)) {
            HorizontalDivider(
                color = Color(0xFF252525),
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
            CompositionLocalProvider {
                TabRow(selectedTabIndex = tabIndex.value!!, indicator = {}, divider = {}) {
                    viewModel.tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = tabIndex.value!! == index,
                            onClick = { viewModel.updateTabIndex(index) },
                            selectedContentColor = Color.White,
                            icon = {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
                                ) {

                                    Icon(
                                        imageVector =
                                        when (index) {
                                            0 -> if (tabIndex.value == index) ImageVector.vectorResource(id = R.drawable.home_filled) else ImageVector.vectorResource(id = R.drawable.home_outline)
                                            1 -> if (tabIndex.value == index) ImageVector.vectorResource(id = R.drawable.settings_filled) else ImageVector.vectorResource(id = R.drawable.settings_outline)
                                            else -> if (tabIndex.value == index) Icons.Filled.Home else Icons.Outlined.Home
                                        },
                                        contentDescription = "Custom Vector Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    if (tabIndex.value == index) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color.White, shape = CircleShape)
                                                .height(2.dp)
                                                .width(30.dp)
                                                .align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    UpdateChecker()
}
