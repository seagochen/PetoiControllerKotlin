package com.petoi.kotlin.android.app

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


const val ENABLE_BLUETOOTH_REQUEST_CODE     = 1
const val LOCATION_PERMISSION_REQUEST_CODE  = 2


class BluetoothHandler {

    // ble适配器
    val adapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    // ble搜索器
    val bleScanner by lazy {
        adapter.bluetoothLeScanner
    }

    // 外置ble设备
    val peripherals = HashMap<String, BluetoothDevice>()

    // 注册消息回调函数--设备查找结果
    val scanCallback = LeScanCallback()

    // 搜索条件
    val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    var isScanning: Boolean = false

    // 检测蓝牙设备是否可用，如果不可用弹出提示框，并跳转至蓝牙设置界面（系统默认）
    fun promptEnableBluetooth(activity: AppCompatActivity) {
        if (!adapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    // 开始搜索设备
    fun startScanPeripherals() {
        peripherals.clear()
        adapter.bluetoothLeScanner.startScan(null, scanSettings, scanCallback)
        isScanning = true
    }

    // 停止搜索设备
    fun stopScanPeripherals() {
        adapter.bluetoothLeScanner.stopScan(scanCallback)
        isScanning = false
    }

    // 返回给用户查找到的设备名称
    fun deviceNames(): MutableList<String> {
        val devices = mutableListOf<String>()

        for (item in peripherals) {
            devices.add(item.key)
        }

        print(devices)

        return devices
    }


    // BLE设备搜索回调类
    inner class LeScanCallback: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            val device = result?.device

            if (device != null && device.name != null && device.address != null) {
                // for debug
                Log.d("LeScanCallback", "found device: ${device.name} address: ${device.address}")

                // append device to the list
                peripherals.put(device.name, device)
            }
        }
    }

    // BLE设备，消息回调类
    inner class LeBluetoothGattCallback: BluetoothGattCallback() {

        // 与BLE设备连结后，设备连结状态发生改变时
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
        }

        // 向BLE设备发送信号
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        // 从BLE设备读取数据
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
        }

        // BLE设备的Characteristic发生改变时
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
        }

        // 发现服务
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
        }
    }
}