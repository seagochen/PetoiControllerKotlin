package com.petoi.kotlin.android.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity

class MotionsControlActivity : BluetoothBasedActivity() {

    fun bindBackBtn() {
        val btn = findViewById<Button>(R.id.btn_motion_back)
        btn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun bindTestBtn() {
        //TODO
    }

    fun bindSaveBtn() {
        //TODO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion)

        // bind widgets
        bindBackBtn()
        bindTestBtn()
        bindSaveBtn()
    }
}