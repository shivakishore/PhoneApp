package com.shiva.phoneapp.conference

import android.net.Uri
import android.telecom.Conference
import android.telecom.PhoneAccountHandle
import android.util.Log

/**
 * Created by Shiva Kishore on 3/1/2022.
 */
class PhoneConference(phoneAccount: PhoneAccountHandle?) : Conference(phoneAccount) {

    override fun onAddConferenceParticipants(participants: MutableList<Uri>) {
        super.onAddConferenceParticipants(participants)
        Log.d("CONF", "onAddConferenceParticipants $participants")
    }


}