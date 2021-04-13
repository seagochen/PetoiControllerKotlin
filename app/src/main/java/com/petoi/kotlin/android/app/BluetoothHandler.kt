package com.petoi.kotlin.android.app

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
open class BluetoothHandler: ScanCallback() {

    // ble适配器
    protected val adapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    // 外置ble设备列表
    private val peripherals = HashMap<String, BluetoothDevice>()

    // 通用属性协议，定义了BLE通讯的基本规则和操作
    private var selectedGatt: BluetoothGatt? = null



    // 开始搜索设备
    fun startScanPeripherals() {

        // 搜索条件
        val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

        peripherals.clear()
        adapter.bluetoothLeScanner.startScan(null, scanSettings, this)
    }


    // 停止搜索设备
    fun stopScanPeripherals() {
        adapter.bluetoothLeScanner.stopScan(this)
    }


    // 返回给用户查找到的设备名称
    fun deviceNames(): MutableList<String> {
        val devices = mutableListOf<String>()

        for (item in peripherals) {
            devices.add(item.key)
        }
        return devices
    }


    // 通过设备名连结到给定的设备
    fun connectDeviceByName(context: Context, name: String) {
        if (! peripherals.containsKey(name)) return // 不存在指定的设备

        // 断开设备连结
        disconnectDevice()

        // 有指定的设备
        val device = peripherals[name]

        // 连结至BLE设备
        if (device != null) {
            selectedGatt = device.connectGatt(context, false, BlueGattCallbackImp())
        }
    }


    // 和BLE设备断开连结
    fun disconnectDevice() {
        if (selectedGatt != null) {
            selectedGatt?.disconnect()
            selectedGatt?.close()
        }
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