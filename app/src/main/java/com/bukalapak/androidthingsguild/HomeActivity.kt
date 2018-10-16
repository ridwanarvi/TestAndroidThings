package com.bukalapak.androidthingsguild

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.PeripheralManager

private val TAG = HomeActivity::class.java.simpleName

class HomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = PeripheralManager.getInstance()
        Log.d(TAG, "GPIOs: " + manager.gpioList)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
