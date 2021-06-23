package com.petoi.kotlin.android.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import com.petoi.kotlin.android.app.database.DatabaseHelper

class MotionsEditActivity : BluetoothBasedActivity() {

    // private val sqliteDB = DatabaseHelper(this)

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

    override fun onBackPressed() {
        super.onBackPressed()

        // 跳转回MainActivity
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}