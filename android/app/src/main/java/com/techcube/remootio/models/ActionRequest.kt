package com.techcube.remootio.models

enum class ActionStatus {
    OPEN,
    CLOSE,
    KEEP_OPENED
}

data class Action(
    val status: ActionStatus,
    var timestamp: Long
)
