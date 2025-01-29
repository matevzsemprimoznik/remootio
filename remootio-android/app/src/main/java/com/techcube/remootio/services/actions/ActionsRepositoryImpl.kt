package com.techcube.remootio.services.actions

import com.google.gson.Gson
import com.techcube.remootio.models.Action
import com.techcube.remootio.models.ActionRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class ActionsRepositoryImpl : ActionsRepository {
    private var apiUrl = ""

    override fun init(apiUrl: String) {
        this.apiUrl = apiUrl
    }

    override fun sendAction(action: Action) {
        val client = OkHttpClient()

        val requestBody = Gson().toJson(ActionRequest(Action.OPEN))

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = requestBody.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(apiUrl)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    println("Response: $responseBody")
                } else {
                    println("Request failed with code: ${response.code}")
                }
            }
        })
    }
}