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
class BluetoothHandler: ScanCallback() {

    // ble适配器
    private val adapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }


    // ble搜索器
    private val bleScanner by lazy {
        adapter.bluetoothLeScanner
    }


    // 外置ble设备列表
    val peripherals = HashMap<String, BluetoothDevice>()


    // 通用属性协议，定义了BLE通讯的基本规则和操作
    private var selectedGatt: BluetoothGatt? = null


    // 注册一个BLE连结消息回调函数
//    var mServiceList: List<BluetoothGattService>? = null


    // 搜索条件
    val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()


    // 蓝牙连结状态
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
        bleScanner.startScan(null, scanSettings, this)
        isScanning = true
    }


    // 停止搜索设备
    fun stopScanPeripherals() {
        bleScanner.stopScan(this)
        isScanning = false
    }


    // 返回给用户查找到的设备名称
    fun deviceNames(): MutableList<String> {
        val devices = mutableListOf<String>()

        for (item in peripherals) {
            devices.add(item.key)
        }
        return devices
    }


    // 和BLE设备断开连结
    fun disconnectDevice() {
        if (selectedGatt != null) {
            selectedGatt?.disconnect()
            selectedGatt?.close()
        }
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