package com.techcube.remootio.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.techcube.remootio.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@Composable
fun UpdateChecker() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var apkUrl by remember { mutableStateOf("") }

    val currentVersion = BuildConfig.VERSION_CODE
    val versionMetadataUri = BuildConfig.VERSION_METADATA_URI

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(versionMetadataUri).build()
                val response = client.newCall(request).execute()

                val json = JSONObject(response.body?.string() ?: return@withContext)
                val latestVersion = json.getInt("latestVersion")
                val url = json.getString("apkUrl")

                Log.d("UpdateChecker", "Update check started" +
                        " Current Version: $currentVersion, Update URL: $versionMetadataUri, Latest Version: $latestVersion, APK URL: $url")

                if (latestVersion > currentVersion) {
                    apkUrl = url
                    showDialog = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    if (showDialog) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(
                        text = "Update Available",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                text = {
                    Text(
                        text = "There is a new version of the app available. " +
                                "Do you want to download it?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        openInBrowser(context, apkUrl)
                        showDialog = false
                    }) {
                        Text("Download")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

private fun openInBrowser(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
    }
}