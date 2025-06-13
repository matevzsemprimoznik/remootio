package com.techcube.remootio.services.actions

import com.google.firebase.database.DatabaseReference
import com.techcube.remootio.models.Action
import com.google.firebase.database.FirebaseDatabase

class ActionsRepositoryImpl : ActionsRepository {
    private var actions: DatabaseReference = FirebaseDatabase.getInstance().getReference("actions")

    override fun sendAction(action: Action) {
        val actionId = actions.push().key

        if (actionId != null) {
            actions.child(actionId).setValue(action)
        }
    }
}