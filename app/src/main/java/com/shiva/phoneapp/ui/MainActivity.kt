package com.shiva.phoneapp.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.telecom.TelecomManager
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessaging
import com.shiva.phoneapp.CallUtils.Companion.ACTION_ANSWER_CALL
import com.shiva.phoneapp.CallUtils.Companion.ACTION_END_CALL
import com.shiva.phoneapp.CallUtils.Companion.ACTION_REJECT_CALL
import com.shiva.phoneapp.MyForeGroundService
import com.shiva.phoneapp.NotificationUtils
import com.shiva.phoneapp.R
import com.shiva.phoneapp.databinding.ActivityMainBinding
import com.shiva.phoneapp.phone.CallState
import com.shiva.phoneapp.phone.MyService


class MainActivity : AppCompatActivity() {
    private lateinit var manager: TelecomManager
    private lateinit var mWakeLock: PowerManager.WakeLock
    private lateinit var binding: ActivityMainBinding
    private lateinit var mService: MyForeGroundService
    private var mBound: Boolean = false

    private val viewModel: CallViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        manager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        mWakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
            "myapp:wakelock"
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        NotificationUtils.cancelCallNotification(applicationContext)
        intent.action?.let { callActions(it) }
    }

    override fun onResume() {
        mWakeLock.acquire(10 * 60 * 100L /*10 minutes*/)
        super.onResume()
        Log.d("STATE", "${MyService.callConnection.state}")
        Log.d("CONNECTIONS", "${MyService.callConnectionList.size}")

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener(OnSuccessListener<Any> { instanceIdResult ->
                val token: String = instanceIdResult.toString()
                Log.d("FCM", "token: $token")
            })
        CallState.currentState.observe(this) {
            Log.d("Current STATE", "$it")
            viewModel.callState.value = it
        }
    }

    override fun onDestroy() {
        mWakeLock.release()
        viewModel.rejectCall()
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(serviceConnection)
        }
        NotificationUtils.cancelInCallNotification(applicationContext)
        mBound = false
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MAINACTIVITY", "onNewIntent ${intent.extras} action: ${intent.action}")
        intent.action?.let { callActions(it) }
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MyForeGroundService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    fun callActions(action: String) {
        when (action) {
            ACTION_ANSWER_CALL -> {
                Log.i("MAINACTIVITY", "onReceive - answerCall ${MyService.callConnection.state}")
                viewModel.acceptCall()
                Intent(this, MyForeGroundService::class.java).also { intent ->
                    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
                    startForegroundService(intent)
                }
            }
            ACTION_REJECT_CALL -> {
                Log.i("MAINACTIVITY", "onReceive - rejectCall")
                viewModel.rejectCall()
                if(mBound){
                    mService.stopForeground(true)
                }
                finish()
            }
            ACTION_END_CALL -> {
                Log.i("MAINACTIVITY", "onReceive - endCall")
                viewModel.rejectCall()
                if(mBound){
                    mService.stopForeground(true)
                }
                finish()
            }
        }
    }
}
