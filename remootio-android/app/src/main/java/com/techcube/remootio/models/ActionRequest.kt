package com.techcube.remootio.models

import com.google.gson.annotations.SerializedName

enum class Action {
    @SerializedName("OPEN") OPEN,
    @SerializedName("CLOSE") CLOSE,
    @SerializedName("KEEP_OPENED") KEEP_OPENED
}

data class ActionRequest(
    val action: Action
)
