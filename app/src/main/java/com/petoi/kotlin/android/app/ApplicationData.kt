package com.petoi.kotlin.android.app

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi

class ApplicationData : Application() {

    companion object {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        val bleHandler: BluetoothHandlerExt = BluetoothHandlerExt()
    }
}