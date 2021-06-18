package com.petoi.kotlin.android.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity

class CalibrationActivity : BluetoothBasedActivity() {

    // 退回按钮
    private fun bindBackBtn() {
        val btn = findViewById<Button>(R.id.btn_calib_back)
        btn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibration)

        bindBackBtn()
    }
}