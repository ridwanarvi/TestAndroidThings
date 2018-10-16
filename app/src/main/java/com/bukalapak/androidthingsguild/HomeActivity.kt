package com.bukalapak.androidthingsguild

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.google.firebase.firestore.FirebaseFirestore

private val TAG = HomeActivity::class.java.simpleName
private const val GPIO_RELAY = "BCM5"

class HomeActivity : Activity() {
    private var relay: Gpio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGPIO()

        val db = FirebaseFirestore.getInstance()
        db.collection("androidthings")
            .document("ledStatus")
            .addSnapshotListener { snapshot, e ->
                e?.let {
                    Log.d(TAG, "ERROR", it)
                } ?: run {
                    val status: String = (snapshot?.get("ledState") ?: "LOW").toString()
                    setRelay(status == "HIGH")
                }
            }
    }

    private fun initGPIO() {
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

    private fun setRelay(on: Boolean) {
        Log.d(TAG, "status: $on")
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
