package com.petoi.kotlin.android.app

import android.app.Application

class ApplicationData : Application() {

    companion object {
        val bleHandler: BluetoothHandler = BluetoothHandler()
    }
}