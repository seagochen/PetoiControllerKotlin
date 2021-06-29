package com.petoi.kotlin.android.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.petoi.kotlin.android.app.properties.PetoiDevice
import com.petoi.kotlin.android.app.properties.PetoiPreferences

class DeviceSelectionActivity: AppCompatActivity() {

    private lateinit var preferences: PetoiPreferences

    private fun jumpToLegalInfo() {
        intent = Intent(this@DeviceSelectionActivity, LegalInfoActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun bindSelectableDeviceBtns() {
        val btn1 = findViewById<ImageButton>(R.id.ibt_select_device1)
        val btn2 = findViewById<ImageButton>(R.id.ibt_select_device2)

        preferences = PetoiPreferences(this)

        btn1.setOnClickListener {
            preferences.setSelectedPetoiDevice(this@DeviceSelectionActivity, PetoiDevice.Bittle)
            jumpToLegalInfo()
        }

        btn2.setOnClickListener {
            preferences.setSelectedPetoiDevice(this@DeviceSelectionActivity, PetoiDevice.Nybble)
            jumpToLegalInfo()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_device)

        // 绑定设备
        bindSelectableDeviceBtns()
    }
}