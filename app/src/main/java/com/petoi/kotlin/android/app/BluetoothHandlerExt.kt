package com.petoi.kotlin.android.app

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


const val ENABLE_BLUETOOTH_REQUEST_CODE     = 1
const val LOCATION_PERMISSION_REQUEST_CODE  = 2


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BluetoothHandlerExt: BluetoothHandler() {



    // 检测蓝牙设备是否可用，如果不可用弹出提示框，并跳转至蓝牙设置界面（系统默认）
    fun promptEnableBluetooth(activity: AppCompatActivity) {
        if (!super.adapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }






}