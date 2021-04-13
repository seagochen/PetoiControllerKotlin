package com.petoi.kotlin.android.app

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class BlueGattCallbackImp : BluetoothGattCallback() {

    // 与BLE设备连结后，设备连结状态发生改变时
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)

        Log.d("GattCallback", "onConnectionStateChange status:$status  newState:$newState");
        if (status == 0) {
            gatt?.discoverServices();
        }
    }


    // 向BLE设备发送信号
    override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int) {
        super.onCharacteristicWrite(gatt, characteristic, status)

        Log.d("GattCallback", "onCharacteristicWrite");
    }


    // 从BLE设备读取数据
    override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int) {
        super.onCharacteristicRead(gatt, characteristic, status)

        Log.d("GattCallback", "onCharacteristicRead");
    }


    // BLE设备的Characteristic发生改变时
    override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?) {
        super.onCharacteristicChanged(gatt, characteristic)

        Log.d("GattCallback", "onCharacteristicChanged");
    }


    // 发现服务
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)

        Log.d("GattCallback", "onServicesDiscovered status$status")

//            mServiceList = gatt!!.services
        for (service in gatt!!.services) {
            Log.d("::onServicesDiscovered", service.toString())
        }

//            if (mServiceList != null) {
//                Log.d("::onServicesDiscovered", mServiceList)
//                Log.d("::onServicesDiscovered", "Services num:" + mServiceList.size())
//            }
//
//            for (service in mServiceList) {
//                val characteristics = service.characteristics
//                println("扫描到Service：" + service.uuid)
//                for (characteristic in characteristics) {
//                    println("characteristic: " + characteristic.uuid)
//                }
//            }
    }
}