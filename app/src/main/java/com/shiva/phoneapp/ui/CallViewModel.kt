package com.shiva.phoneapp.ui

import android.telecom.Connection
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shiva.phoneapp.phone.CallState
import com.shiva.phoneapp.phone.MyService

/**
 * Created by Shiva Kishore on 3/1/2022.
 */
class CallViewModel : ViewModel() {

    val callState = MutableLiveData<Int>()

    fun disconnectCall() {
        MyService.callConnection.onDisconnect()
    }
    fun mergeCalls() {
        MyService.callConnection.mergeCalls()
    }
    fun acceptCall() {
        MyService.callConnection.onAnswer()
    }

    fun rejectCall() {
        MyService.callConnection.onReject()
    }

/*    fun getCallStatus(): String {
        return when (CallState.currentState.value) {
            Connection.STATE_RINGING -> "incoming call"
            Connection.STATE_ACTIVE -> "call inprogress"
            Connection.STATE_DISCONNECTED -> "call disconnected"
            else -> "No call"
        }
    }*/
}