package com.techcube.remootio.services
import com.techcube.remootio.services.actions.ActionsRepository
import com.techcube.remootio.services.actions.ActionsRepositoryImpl

object Repositories {
    var actionRepository: ActionsRepository = ActionsRepositoryImpl()
}