package com.shiva.phoneapp.phone

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.telecom.CallAudioState
import android.telecom.Connection
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.util.Log
import com.shiva.phoneapp.NotificationUtils
import com.shiva.phoneapp.PhoneApplication
import com.shiva.phoneapp.conference.PhoneConference

class CallConnection(private val context: Context) : Connection() {
    init {
        connectionProperties = PROPERTY_SELF_MANAGED;
        audioModeIsVoip = true;
        connectionCapabilities = connectionCapabilities or CAPABILITY_HOLD or CAPABILITY_MANAGE_CONFERENCE

    }

    private val TAG = "CallConnection"

    override fun onShowIncomingCallUi() {
        Log.i(TAG, "onShowIncomingCallUi $this")
        super.onShowIncomingCallUi()
        setRinging()
        CallState.state = STATE_RINGING
        if (MyService.callConnectionList.size == 1) {
            NotificationUtils.showIncomingCallNotification(context)
        }
    }

    override fun onCallAudioStateChanged(state: CallAudioState?) {
        Log.i(TAG, "onCallAudioStateChanged ${state.toString()}")
    }

    override fun onAnswer() {
        setActive()
        Log.i(TAG, "onAnswer $this")
        CallState.state = STATE_ACTIVE
    }

    override fun onDisconnect() {
        setDisconnected(DisconnectCause(DisconnectCause.REMOTE))
//        removeNotification()
        Log.i(TAG, "onDisconnect $this")
        destroy()
        CallState.state = STATE_DISCONNECTED
        MyService.callConnectionList.remove(this)
    }

    override fun onHold() {
        setOnHold()
        Log.i(TAG, "onHold $this")
        CallState.state = STATE_HOLDING
    }

    override fun onUnhold() {
        setActive()
        Log.i(TAG, "onUnhold $this")
        CallState.state = STATE_ACTIVE
    }

    override fun onReject() {
        MyService.callConnectionList.remove(this)
        setDisconnected(DisconnectCause(DisconnectCause.REJECTED))
//        removeNotification()
        Log.i(TAG, "onReject $this")
        destroy()
        CallState.state = STATE_DISCONNECTED
    }


    fun mergeCalls() {

        setConferenceableConnections(MyService.callConnectionList as List<Connection>?)
        val phoneAccountHandle = PhoneAccountHandle(
            ComponentName(PhoneApplication.appContext, MyService::class.java.name),
            "myCallHandlerId"
        )

        val phoneConference = PhoneConference(phoneAccountHandle)
        MyService.callConnectionList.forEach {
            phoneConference.addConnection(it)
        }
        phoneConference.setActive()
    }

    override fun onAddConferenceParticipants(participants: MutableList<Uri>) {
        super.onAddConferenceParticipants(participants)
        Log.i(TAG, "onAddConferenceParticipants $participants")
    }
}