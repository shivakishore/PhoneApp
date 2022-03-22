package com.shiva.phoneapp

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.telecom.ConnectionService
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import com.shiva.phoneapp.phone.MyService

/**
 * Created by Shiva Kishore on 2/12/2022.
 */
class CallUtils {
    companion object {
        const val ACTION_ANSWER_CALL = "com.shiva.phoneapp.phone.action.ANSWER_CALL"
        const val ACTION_REJECT_CALL = "com.shiva.phoneapp.phone.action.REJECT_CALL"
        const val ACTION_END_CALL = "com.shiva.phoneapp.phone.action.END_CALL"

/*
        fun registerPhoneAccount(context: Context): Boolean {

            val phoneAccountHandle = PhoneAccountHandle(
                ComponentName(context.packageName, MyService::class.java.name),
                "myCallHandlerId"
            )
            val phoneAccount = PhoneAccount
                .builder(phoneAccountHandle, "myCallHandlerId")
                .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
                .build()
            val manager: TelecomManager =
                context.getSystemService(ConnectionService.TELECOM_SERVICE) as TelecomManager
            manager.registerPhoneAccount(phoneAccount)
            return manager.getPhoneAccount(phoneAccountHandle).isEnabled
        }
*/

        fun addNewIncomingCall(context: Context, phoneNumber: String?) {
            val phoneAccountHandle = PhoneAccountHandle(
                ComponentName(context.packageName, MyService::class.java.name),
                "myCallHandlerId"
            )
            val phoneAccount = PhoneAccount
                .builder(phoneAccountHandle, "myCallHandlerId")
                .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
                .build()
            val manager: TelecomManager =
                context.getSystemService(ConnectionService.TELECOM_SERVICE) as TelecomManager
            manager.registerPhoneAccount(phoneAccount)
            Log.i("Phone Account", "" + manager.getPhoneAccount(phoneAccountHandle))
            Log.i("Phone Account", "" + manager.getPhoneAccount(phoneAccountHandle).isEnabled)
            Log.i("Phone Account", "" + manager.getPhoneAccount(phoneAccountHandle).javaClass)
            Log.i("Phone Account isEnabled", "" + phoneAccount.isEnabled)

            val bundle = Bundle()
            val uri: Uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, phoneNumber, null)
            bundle.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri)
            bundle.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
//        bundle.putString("from", "9701515051")

            Log.i("Permitted", "" + manager.isIncomingCallPermitted(phoneAccountHandle))
            Log.i("Call", "Incoming")
            if (manager.getPhoneAccount(phoneAccountHandle).isEnabled) {
                Log.d("MyService.callConnection state", "${MyService.callConnection.state}")
                manager.addNewIncomingCall(phoneAccountHandle, bundle)
            } else {
                Log.i("Call", "DISABLED.!!")
            }
        }
    }
}