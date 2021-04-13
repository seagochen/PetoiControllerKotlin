package com.petoi.kotlin.android.app

import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.concurrent.schedule

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : BluetoothBasedActivity() {

    // 用户选择的蓝牙设备名字
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

                // 停止ble查找，并打印日志
                stopBleScan()

                // 按钮可用
                runOnUiThread {
                    searchBtn.isEnabled = true

                    val keys = foundDeviceNames()

                    if (keys.size > 0) {

                        popupSelectableMenus(keys.toList()) { value ->
                            bleDeviceSelected = keys[value]
                        }
                    }
                }
            }
        }
    }

    // 连结BLE设备
    private lateinit var connectBtn: Button

    // 绑定连结按钮功能
    private fun bindConnectBtnView() {
        connectBtn = findViewById(R.id.connectBtn)

        connectBtn.setOnClickListener {

            if (connectBtn.text == "Connect") {
                connect(bleDeviceSelected)

                // update text to "disconnect"
                "Disconnect".also { connectBtn.text = it }
            } else {
                disconnect()

                // update text to connect
                "Connect".also { connectBtn.text = it }
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
}