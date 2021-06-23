package com.petoi.kotlin.android.app.bluetooth

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.petoi.kotlin.android.app.ApplicationData
import kotlin.concurrent.thread


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
open class BluetoothBasedActivity: AppCompatActivity() {

    // 从Application获得bluetooth管理器
    private val handler: BluetoothHandlerExt = ApplicationData.bleHandler

    // 从Application获得校准处理模块
    private val calib = ApplicationData.calibration

    // 用于更新输出的内容
    private var feedbackTextview: TextView? = null

    // 用于控制子线程的标识符
    private var isTvUpdateRunning = false

    // 用于输出的缓冲字符串
    private var feedback: String = ""

    // 存放用户最近的指令
    private var lastCmd: String = ""


    // 确定 ACCESS_FINE_LOCATION 权限是否被赋予了App
    // 它是只读的属性，每次用户请求的时候计算一次
    private val isLocationPermissionGranted
        get() = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)


    // 该函数是用来确定给定的权限是否已经被赋予
    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }

//    // 弹出可选菜单
//    protected fun popupSelectableMenus(items: List<String>, callback: (Int) -> Unit) {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this@BluetoothBasedActivity)
//
//        builder.setItems(
//                items.toTypedArray()
//        ) { dialog, index ->
//            // 把用户选择的设备编号通过回调函数的形式返回给用户
//            callback(index)
//
//            // 把对话框关闭
//            dialog.dismiss()
//        }
//        builder.show()
//    }


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
    protected fun startBleScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionGranted) {
            requestLocationPermission()
        }
        else {
            handler.startScanPeripherals()
        }
    }

    // 停止ble搜索
    protected fun stopBleScan() {
        handler.stopScanPeripherals()
    }

    // 返回给用户查找到的蓝牙设备名字
    protected fun foundDeviceNames(): MutableList<String> {
        return handler.deviceNames()
    }

    // 连结设备
    protected fun connect(name: String) {
        handler.connectDeviceByName(this@BluetoothBasedActivity, name)
    }

    // 断开连结
    protected fun disconnect() {
        handler.disconnectDevice()
    }

    // 发送数据
    protected fun send(msg: String) {

        // 如果用户发送校准命令，立即清空缓存
        if (msg == "c") {
            handler.clear()
        }

        // 发送指令
        handler.send(msg)

        // 更新指令
        lastCmd = msg
    }

    // 检测是否与BLE设备建立了连接
    protected  fun isConnected(): Boolean {
        return handler.isConnected()
    }



    // 使用线程函数创建线程
    private val tvUpdateThread: Thread = thread {
        while(isTvUpdateRunning) {

            // 尝试更新内容
            feedback += handler.recv()
            runOnUiThread {
                if (feedbackTextview != null && feedback != "") {
                    feedbackTextview!!.setText(feedback)
                }
            }

            // 当用户发送c指令后，需要对这个指令的反馈信息进行专门处理
            if (lastCmd == "c") {
                if (calib.updateCalibrationInfo(feedback)) {
                    calib.debug() // 打印debug信息
                    break // 已经获得了有效数据，结束循环
                }
            }

            // 休息
            Thread.sleep(10)
        }
    }

    // textview 反馈部分
    protected fun setTextView(tv: TextView) {
        // 更新textview，并重新执行子线程更新任务
        feedbackTextview = tv
    }

    // 启动C监听
    protected fun startCalibListening() {

        // 先关闭
        stopCalibListening()

        // 更新textview，并重新执行子线程更新任务
        isTvUpdateRunning = true
        tvUpdateThread.start()
    }

    // 关闭C监听
    protected fun stopCalibListening() {
        // 关闭正在执行的线程，并等待线程退出
        if (isTvUpdateRunning) {
            isTvUpdateRunning = false
            tvUpdateThread.join()
        }
    }

    // 向用户发起权限请求
    private fun requestLocationPermission() {
        if (isLocationPermissionGranted) {
            return
        }
        runOnUiThread {
            val alert = AlertDialog.Builder(this)
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