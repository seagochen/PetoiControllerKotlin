package com.petoi.kotlin.android.app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import java.util.*
import kotlin.concurrent.schedule

class ConnectionActivity : BluetoothBasedActivity() {

    // 用户选择的蓝牙设备名字
    private var bleDeviceSelected: String = ""

    // 可用设备列表
    private var foundDevices = mutableListOf<String>()

    // 设备响应列
    private lateinit var arrayAdapter: ArrayAdapter<String>

    // UI 列表
    private lateinit var listView: ListView

    // 控件绑定
    private lateinit var searchBtn: Button

    // 连结BLE设备
    private lateinit var connectBtn: Button


    // 搜索设备
    private fun searchBleDevices() {

        // 首先设置按钮为不可用
        searchBtn.isEnabled = false

        // 启动ble查找
        startBleScan()

        // 设定一个延时器
        Timer().schedule(1000) {

            // 在主线程中执行如下命令
            runOnUiThread {
                // 重新启用
                searchBtn.isEnabled = true

                // 停止ble查找，并打印日志
                stopBleScan()

                // 更新可用设备列表
                foundDevices.clear()
                for (name in foundDeviceNames()) {
                    foundDevices.add(name)
                }

                // 把列表写入listview中
                if (foundDevices.size > 0) {
                    arrayAdapter.notifyDataSetChanged()
                }
            }

            // 设置列表的点击事件
            listView.setOnItemClickListener{ adapterView, view, position: Int, id: Long ->
                bleDeviceSelected = foundDevices[position]
                adapterView.isSelected = true
            }
        }
    }


    // 绑定搜索按钮功能
    private fun initMajorWidgets() {

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, foundDevices)
        
        listView = findViewById(R.id.list_connect_devices)

        searchBtn = findViewById(R.id.btn_connect_search)

        // 把adapter和list连接起来
        listView.adapter = arrayAdapter

        // list mode
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // 事件绑定
        searchBtn.setOnClickListener {
            searchBleDevices()
        }
    }



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

    // 自动更新窗口组件状态
    private fun updateWidgetStatus() {

        // 自动启动搜索功能
        searchBleDevices()

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
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)

        // 绑定控件
        initMajorWidgets()
        bindConnectBtnView()
        bindBackBtn()

        // 更新控件状态
        updateWidgetStatus()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // 跳转回MainActivity
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}