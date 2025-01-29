package com.techcube.remootio.services

import android.content.Context
import com.techcube.remootio.R
import com.techcube.remootio.services.actions.ActionsRepository
import com.techcube.remootio.services.actions.ActionsRepositoryImpl

object Repositories {
    var actionRepository: ActionsRepository = ActionsRepositoryImpl()

    fun init(context: Context) {
        actionRepository.init(context.getString(R.string.action_api_url))
    }
}