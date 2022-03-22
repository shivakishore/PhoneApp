package com.shiva.phoneapp.phone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Created by Shiva Kishore on 2/6/2022.
 */
class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0, "RebootReceiver", Toast.LENGTH_LONG).show()
    }
}