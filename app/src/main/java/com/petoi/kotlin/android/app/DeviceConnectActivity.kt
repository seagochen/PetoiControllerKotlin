package com.petoi.kotlin.android.app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import java.util.*
import kotlin.concurrent.schedule

class DeviceConnectActivity : BluetoothBasedActivity() {

    // 用户选择的蓝牙设备名字
    private var bleDeviceSelected: String = ""

    // 蓝牙设备搜索按钮
    private lateinit var searchBtn: Button

    // 可用设备列表
    private lateinit var foundDevices: MutableList<String>

    // 绑定搜索按钮功能
    private fun bindSearchBtnView() {
        // 控件绑定
        searchBtn = findViewById(R.id.btn_connect_search)

        // 列表绑定
        val listView = findViewById<ListView>(R.id.list_connect_devices)

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

                    // 可用设备列表
                    foundDevices = foundDeviceNames()

                    // 把列表写入listview中
                    if (foundDevices.size > 0) {

                        val arrayAdapter = ArrayAdapter(this@DeviceConnectActivity,
                            android.R.layout.simple_list_item_1, foundDevices)

                        listView.adapter = arrayAdapter
                    }

//                    // 弹出式可选菜单
//                    if (keys.size > 0) {
//
//                        popupSelectableMenus(keys.toList()) { value ->
//                            bleDeviceSelected = keys[value]
//                        }
//                    }
                }

                // 设置列表的点击事件
                listView.setOnItemClickListener{ adapterView, view, position: Int, id: Long ->
                    bleDeviceSelected = foundDevices[position]
                }
            }
        }
    }


    // 连结BLE设备
    private lateinit var connectBtn: Button

    // 绑定连结按钮功能
    private fun bindConnectBtnView() {
        connectBtn = findViewById(R.id.btn_connect_connect)

        connectBtn.setOnClickListener {

            if (isConnected()) {

                // 连接到设备
                disconnect()

                // update text to connect
                "Connect".also { connectBtn.text = it }
            } else {
                if (bleDeviceSelected != "") {
                    connect(bleDeviceSelected)

                    // update text to "disconnect"
                    "Disconnect".also { connectBtn.text = it }
                }
            }
        }
    }

    private fun updateWidgetStatus() {
        if (isConnected()) {
            "Disconnect".also { connectBtn.text = it }
        } else {
            "Connect".also { connectBtn.text = it }
        }
    }

    // 退回按钮
    private fun bindBackBtn() {
        val btn = findViewById<Button>(R.id.btn_connect_back)
        btn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)

        // 绑定控件
        bindSearchBtnView()
        bindConnectBtnView()
        bindBackBtn()

        // 更新控件状态
        updateWidgetStatus()
        //TODO 蓝牙连接状态检测有点异常
    }
}