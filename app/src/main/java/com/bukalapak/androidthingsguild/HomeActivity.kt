package com.bukalapak.androidthingsguild

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.google.firebase.firestore.FirebaseFirestore

private val TAG = HomeActivity::class.java.simpleName

class HomeActivity : Activity() {
    val GPIO_RELAY = "BCM5"

    private var relay: Gpio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = FirebaseFirestore.getInstance()
        db.collection("androidthings")
            .document("ledStatus")
            .addSnapshotListener { snapshot, e ->
                Log.d(TAG, "snapshot:$snapshot")
                e?.let {
                    Log.d(TAG, "ERROR", it)
                } ?: run {
                    Log.d(TAG, "LED state: " + snapshot?.get("ledState"))
                }
            }

        val manager = PeripheralManager.getInstance()
        Log.d(TAG, "GPIOs: " + manager.gpioList)

        try {
            relay = manager.openGpio(GPIO_RELAY)
            relay?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            relay?.setActiveType(Gpio.ACTIVE_LOW)
        } catch (e: Exception) {
            Log.w(TAG, "Unable to access GPIO", e)
        }

    }

    fun setRelay(on: Boolean) {
        relay?.value = on
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            relay?.close()
            relay = null
        } catch (e: Exception) {
            Log.w(TAG, "Unable to close GPIO", e)
        }
    }
}
