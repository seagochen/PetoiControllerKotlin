package com.petoi.kotlin.android.app

import android.bluetooth.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class BlueGattCallbackImp : BluetoothGattCallback() {



    var writeCharacteristic:    BluetoothGattCharacteristic? = null
    var notifyCharacteristic:   BluetoothGattCharacteristic? = null


    // 与BLE设备连结后，设备连结状态发生改变时
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)

        val deviceAddress = gatt?.device?.address

        if (status == BluetoothGatt.GATT_SUCCESS) {

            // Discovers services offered by a remote device as well as
            // their characteristics and descriptors.
            gatt?.discoverServices()

            when(newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.w("BluetoothGattCallback", "Successfully connected to $deviceAddress")
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.w("BluetoothGattCallback", "Successfully disconnected from $deviceAddress")
                    gatt?.close()
                }
            }
        } else {
            Log.w("BluetoothGattCallback", "Error $status encountered for $deviceAddress! Disconnecting...")
            gatt?.close()
        }
    }


    // 向BLE设备发送信号
    override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int) {
        super.onCharacteristicWrite(gatt, characteristic, status)

        val uuid = characteristic?.uuid
        val value = characteristic?.value

        val strValue = value?.let { String(it) }

        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                Log.i("BluetoothGattCallback", "Wrote to characteristic $uuid | value: $strValue")
            }
            BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH -> {
                Log.e("BluetoothGattCallback", "Write exceeded connection ATT MTU!")
            }
            BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
                Log.e("BluetoothGattCallback", "Write not permitted for $uuid!")
            }
            else -> {
                Log.e("BluetoothGattCallback", "Characteristic write failed for $uuid, error: $status")
            }
        }
    }


    // 从BLE设备读取数据
    override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int) {
        super.onCharacteristicRead(gatt, characteristic, status)

        val uuid = characteristic?.uuid
        val value = characteristic?.value

        val strVal = value?.let { String(it) }

        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                Log.i("BluetoothGattCallback", "Read characteristic $uuid:\n$strVal")
            }
            BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
                Log.e("BluetoothGattCallback", "Read not permitted for $uuid!")
            }
            else -> {
                Log.e("BluetoothGattCallback", "Characteristic read failed for $uuid, error: $status")
            }
        }
    }


    // BLE设备的Characteristic发生改变时
    override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?) {
        super.onCharacteristicChanged(gatt, characteristic)

        val uuid = characteristic?.uuid
        val value = characteristic?.value

        val strValue = value?.let { String(it) }

        Log.i("BluetoothGattCallback", "Characteristic $uuid changed | value: $strValue")
    }


    // 发现服务
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)

        val services = gatt?.services
        val device = gatt?.device

        Log.w("BluetoothGattCallback", "Discovered ${services?.size} services for ${device?.address}")

        if (services != null) {
            printGattTable(services) // See implementation just above this section

            // 注册ble服务
            setupBLEService(gatt, services)
        }
    }


    // 打印服务
    private fun printGattTable(services: MutableList<BluetoothGattService>) {
        if (services.isEmpty()) {
            Log.i("printGattTable", "No service and characteristic available, call discoverServices() first?")
            return
        }

        services.forEach { service ->
            val characteristicsTable = service.characteristics.joinToString(
                    separator = "\n|--",
                    prefix = "|--"
            ) { it.uuid.toString() }

            Log.i("printGattTable", "\nService ${service.uuid}\nCharacteristics:\n$characteristicsTable")
        }
    }


    // 设置BLE收发服务及特征
    private fun setupBLEService(gatt: BluetoothGatt, services: MutableList<BluetoothGattService>) {

        // 设置serviceUUID
        val service = gatt.getService(UUID.fromString(Companion.SERVICES_UUID))
        // 设置写入特征UUID
        writeCharacteristic = service.getCharacteristic(UUID.fromString(WRITE_UUID))
        // 设置监听特征UUID
        notifyCharacteristic = service.getCharacteristic(UUID.fromString(NOTIFY_UUID))


        // 开启监听
        gatt.setCharacteristicNotification(notifyCharacteristic,true)

        // 设置消息
        val descriptor = notifyCharacteristic?.getDescriptor(UUID.fromString(NOTIFY_SERVICE_UUID))
        descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
        gatt.writeDescriptor(descriptor)

        // debug
        Log.d("BluetoothGattCallback", "Connection to server done");
    }

    companion object {
        private const val SERVICES_UUID   = "0000ffe0-0000-1000-8000-00805f9b34fb"    //服务的UUID
        private const val WRITE_UUID      = "0000ffe2-0000-1000-8000-00805f9b34fb"    //写入特征的UUID
        private const val NOTIFY_UUID     = "0000ffe1-0000-1000-8000-00805f9b34fb"    //监听特征的UUID
        private const val NOTIFY_SERVICE_UUID = "00002902-0000-1000-8000-00805f9b34fb" // service
    }
}