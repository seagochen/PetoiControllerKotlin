package com.petoi.kotlin.android.app

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BluetoothScanCallbackImp:  ScanCallback() {

    // 外置ble设备列表
    val peripherals = HashMap<String, BluetoothDevice>()

    // 清理设备列表
    fun cleanPerals() {
        peripherals.clear()
    }

    // BLE设备搜索回调类
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)

        val device = result?.device

        if (device != null && device.name != null && device.address != null) {
            // for debug
            Log.d("ScanCallback", "found device: ${device.name} address: ${device.address}")

            // append device to the list
            peripherals[device.name] = device
        }
    }
}