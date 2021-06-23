package com.petoi.kotlin.android.app

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.petoi.kotlin.android.app.bluetooth.BluetoothHandlerExt
import com.petoi.kotlin.android.app.calibration.CalibrationVerification

class ApplicationData : Application() {

    companion object {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        val bleHandler = BluetoothHandlerExt()
        val calibration = CalibrationVerification()
    }
}