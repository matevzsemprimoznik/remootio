package com.techcube.remootio.services.actions

import com.techcube.remootio.models.Action

interface ActionsRepository {
    fun init(apiUrl: String)
    fun sendAction(action: Action)
}