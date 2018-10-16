package com.bukalapak.androidthingsguild

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

private val TAG = HomeActivity::class.java.simpleName

class HomeActivity : Activity() {

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
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
