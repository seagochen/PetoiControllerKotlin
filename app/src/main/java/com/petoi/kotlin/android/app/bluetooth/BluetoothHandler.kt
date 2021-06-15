package com.petoi.kotlin.android.app.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
open class BluetoothHandler {

    // ble适配器
    protected val adapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    // 通用属性协议，定义了BLE通讯的基本规则和操作
    private var selectedGatt: BluetoothGatt? = null

    // bluetooth gattcallback
    private val gattCallback = BluetoothGattCallbackImp()

    // bluetooth scancallback
    private val scanCallback = BluetoothScanCallbackImp()


    // 开始搜索设备
    fun startScanPeripherals() {

        // 搜索条件
        val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

        scanCallback.cleanPerals()
        adapter.bluetoothLeScanner.startScan(null, scanSettings, scanCallback)
    }


    // 停止搜索设备
    fun stopScanPeripherals() {
        adapter.bluetoothLeScanner.stopScan(scanCallback)
    }


    // 返回给用户查找到的设备名称
    fun deviceNames(): MutableList<String> {
        val devices = mutableListOf<String>()

        for (item in scanCallback.peripherals) {
            devices.add(item.key)
        }
        return devices
    }


    // 通过设备名连结到给定的设备
    fun connectDeviceByName(context: Context, name: String) {
        if (! scanCallback.peripherals.containsKey(name)) return // 不存在指定的设备

        // 断开设备连结
        disconnectDevice()

        // 有指定的设备
        val device = scanCallback.peripherals[name]

        // 连结至BLE设备
        if (device != null) {
            selectedGatt = device.connectGatt(context, false, gattCallback)
        }
    }


    // 和BLE设备断开连结
    fun disconnectDevice() {
        if (selectedGatt != null) {
            selectedGatt?.disconnect()
            selectedGatt?.close()
            selectedGatt = null
        }
    }

    // 向BLE设备发送数据
    fun send(msg: String) {
        // 首先清楚缓存数据，避免数据读取出现异常
        gattCallback.cleanMessageList()

        // 获取蓝牙数据写入信道
        val writeChar = gattCallback.writeCharacteristic

        // 发送数据
        if (writeChar != null) {
            writeChar.setValue(msg)
            selectedGatt?.writeCharacteristic(writeChar)
        }
    }

    // 从缓存中抓取数据
    fun recv(): String {
        // 从缓存中读取数据
        if (!gattCallback.isReady()) {
            return ""
        }

        // 从缓存中获取数据
        // TODO 可能会有失败，需要手动调节一个比较合适的等待时间
        return  gattCallback.getMessage()
    }

    // 表示是否已经和设备建立了连接
    fun isConnected(): Boolean {
        return selectedGatt != null
    }
}