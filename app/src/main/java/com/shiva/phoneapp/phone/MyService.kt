package com.shiva.phoneapp.phone

import android.app.Application
import android.content.ComponentName
import android.telecom.*
import android.telecom.Connection.PROPERTY_SELF_MANAGED
import android.telecom.TelecomManager.PRESENTATION_ALLOWED
import android.util.Log
import com.shiva.phoneapp.PhoneApplication

/**
 * Created by Shiva Kishore on 2/5/2022.
 */
class MyService : ConnectionService() {
    val TAG: String = MyService::class.java.name

    companion object {
        var callConnection: CallConnection = CallConnection(PhoneApplication.appContext)
        var callConnectionList: ArrayList<CallConnection> = arrayListOf()
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.i("CallConnectionService", "onCreateIncomingConnection")
        callConnection = CallConnection(applicationContext)
        callConnection.connectionProperties = PROPERTY_SELF_MANAGED
//        conn.setCallerDisplayName("SOMEEONEEIN", PRESENTATION_ALLOWED)
        callConnection.setAddress(request!!.address, PRESENTATION_ALLOWED)
        callConnection.videoState = request.videoState
        callConnection.setInitializing()
        callConnectionList.add(callConnection)
        return callConnection
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
        Log.i("CallConnectionService", "create incoming call failed ")
    }

    override fun onConference(connection1: Connection?, connection2: Connection?) {
        super.onConference(connection1, connection2)
/*
        val phoneAccountHandle = PhoneAccountHandle(
            ComponentName(applicationContext.packageName, MyService::class.java.name),
            "myCallHandlerId"
        )

        val conference : Conference = Conference(phoneAccountHandle)
        addConference()
*/
        Log.i("CallConnectionService", "onConference ")
    }

    override fun onCreateIncomingConference(
        connectionManagerPhoneAccount: PhoneAccountHandle,
        request: ConnectionRequest
    ): Conference? {
        return super.onCreateIncomingConference(connectionManagerPhoneAccount, request)
        Log.i("CallConnectionService", "onCreateIncomingConference ")
    }

    override fun onCreateIncomingConferenceFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateIncomingConferenceFailed(connectionManagerPhoneAccount, request)
        Log.i("CallConnectionService", "onCreateIncomingConferenceFailed ")
    }

    override fun onRemoteConferenceAdded(conference: RemoteConference?) {
        super.onRemoteConferenceAdded(conference)
        Log.i("CallConnectionService", "onRemoteConferenceAdded")
    }

    override fun onRemoteExistingConnectionAdded(connection: RemoteConnection?) {
        super.onRemoteExistingConnectionAdded(connection)
        Log.i("CallConnectionService", "onRemoteExistingConnectionAdded")
    }
}