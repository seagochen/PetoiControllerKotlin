package com.petoi.kotlin.android.app

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.concurrent.schedule

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity() {

    // 从Application获得bluetooth管理器
    private val handler: BluetoothHandler = ApplicationData.bleHandler


    // 确定 ACCESS_FINE_LOCATION 权限是否被赋予了App
    // 它是只读的属性，每次用户请求的时候计算一次
    private val isLocationPermissionGranted
        get() = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)


    // 该函数是用来确定给定的权限是否已经被赋予
    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }

    private lateinit var bleDeviceSelected: String

    // 蓝牙设备搜索按钮
    private lateinit var searchBtn: Button

    // 绑定搜索按钮功能
    private fun bindSearchBtnView() {
        // 控件绑定
        searchBtn = findViewById(R.id.searchBtn)

        // 事件绑定
        searchBtn.setOnClickListener {

            // 首先设置按钮为不可用
            searchBtn.isEnabled = false

            // 启动ble查找
            startBleScan()

            // 设定一个延时器
            Timer().schedule(3000) {
                handler.stopScanPeripherals()
                Log.d("MainActivity::Found", "found devices: ${handler.peripherals.size}")

                // 按钮可用
                runOnUiThread {
                    searchBtn.isEnabled = true

                    val keys = handler.deviceNames()

                    if (keys.size > 0) {

                        popupMenu(keys.toList()) { value ->
                            bleDeviceSelected = keys[value]
                        }
                    }
                }
            }
        }
    }

    // 弹出菜单
    private fun popupMenu(items: List<String>, callback: (Int) -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

        builder.setItems(
            items.toTypedArray()
        ) { dialog, index ->
            // 把用户选择的设备编号通过回调函数的形式返回给用户
            callback(index)
            Log.d("test", "test")

            dialog.dismiss()
        }
        builder.show()
    }


    // 连结BLE设备
    private lateinit var connectBtn: Button

    // 绑定连结按钮功能
    private fun bindConnectBtnView() {
        connectBtn = findViewById(R.id.connectBtn)

        connectBtn.setOnClickListener {

            if (connectBtn.text == "Connect") {
                handler.connectDeviceByName(this@MainActivity, bleDeviceSelected)

                // update text to "disconnect"
                connectBtn.text = "Disconnect"
            } else {
                handler.disconnectDevice()

                // update text to connect
                connectBtn.text = "Connect"
            }
        }
    }


    // Android Activity初始化后的调用函数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 绑定Android控件和它们应该对应的消息事件
        bindSearchBtnView()
        bindConnectBtnView()
    }


    // 在Activity恢复后，蓝牙的连结也要恢复
    override fun onResume() {
        super.onResume()
        handler.promptEnableBluetooth(this)
    }


    // 当从其他页面跳转回当前的页面时，用来确认传入的数值，并处理
    // 比方说，打开了蓝牙控制页面，需要检测是否蓝牙已经正常打开了，否则重新跳转至蓝牙控制页面
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ENABLE_BLUETOOTH_REQUEST_CODE -> {
                if (resultCode != RESULT_OK) {
                    handler.promptEnableBluetooth(this)
                }
            }
        }
    }

    // 授权回调，requestPermission函数的返回处理
    // 处理App的授权请求，如果相关权限请求无法满足，则要求用户打开相关的授权界面
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED) {
                    requestLocationPermission()
                } else {
                    startBleScan()
                }
            }
        }
    }

    // 开始ble搜索功能
    private fun startBleScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionGranted) {
            requestLocationPermission()
        }
        else {
            handler.startScanPeripherals()
        }
    }

    // 向用户发起权限请求
    private fun requestLocationPermission() {
        if (isLocationPermissionGranted) {
            return
        }
        runOnUiThread {
            var alert = AlertDialog.Builder(this)
            alert.setTitle("Location permission required")
                .setMessage(
                    "Starting from Android M (6.0), " +
                            "the system requires apps to be granted location access in " +
                            "order to scan for BLE devices."
                )
                .setCancelable(false).setPositiveButton(android.R.string.ok) { _, _ ->
                    requestPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }.show()
        }
    }

    private fun Activity.requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
}