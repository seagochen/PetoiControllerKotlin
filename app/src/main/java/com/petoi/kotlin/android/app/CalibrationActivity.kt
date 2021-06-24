package com.petoi.kotlin.android.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity

class CalibrationActivity : BluetoothBasedActivity() {

    // 从Application获得校准处理模块
    private val calib = ApplicationData.calibration

    // textview用于数据缓存处理
    private lateinit var tvOutput: TextView

    // 选中的舵机
    private var selectedServo = 0

    // 绑定textview
    private fun bindTvOutput() {
        tvOutput = findViewById<TextView>(R.id.tv_calib_output)
    }

    // 退回按钮
    private fun bindBackBtn() {
        val btn = findViewById<Button>(R.id.btn_calib_back)

        // 监听点击事件
        btn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // 保存按钮
    private fun bindSaveBtn() {
        val saveBtn = findViewById<Button>(R.id.btn_calib_save)

        // 监听点击事件
        saveBtn.setOnClickListener {
            // 保存
            send("s")

            // 跳转回MainActivity
            intent = Intent(this@CalibrationActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // 精度调整
    private fun bindFineAdjustBtn() {
        val addBtn = findViewById<ImageButton>(R.id.btn_calib_add)
        val desBtn = findViewById<ImageButton>(R.id.btn_calib_minus)

        // 先读取当前被选中的舵机角度
        var currentAngle = calib.angle(selectedServo)

        // 定义弹出框警告信息
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder
            .setTitle("Warning")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage("Maximum adjustable fine angle has been reached, remove the steering arm and retry!")
            .setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }

        // fine addition
        addBtn.setOnClickListener {
            val ret = calib.increaseCalibAngle(selectedServo)

            if (!ret.first) {
                alertBuilder.show()
            }

            send(ret.second)
        }

        // fine subtraction
        desBtn.setOnClickListener {
            val ret = calib.decreaseCalibAngle(selectedServo)

            if (!ret.first) {
                alertBuilder.show()
            }

            send(ret.second)
        }
    }

    // 弹出菜单
    private fun bindSevorSelectionBtn() {

        // bind servo button
        val btn = findViewById<Button>(R.id.btn_calib_servor)
        btn.setText(resources.getString(R.string.servo0))

        // convert list to ArrayAdapter
        val datalist = calib.availableServoList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, datalist)

        // create alert dialog and setup its values
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder
            .setTitle("Servo Number")
            .setSingleChoiceItems(adapter, 0) { dialog, which ->
                selectedServo = which
            }
            .setPositiveButton("Ok") { dialog, which ->
                when(selectedServo) {
                    0 -> btn.setText(resources.getString(R.string.servo0))

                    1 -> btn.setText(resources.getString(R.string.servo8))
                    2 -> btn.setText(resources.getString(R.string.servo9))
                    3 -> btn.setText(resources.getString(R.string.servo10))
                    4 -> btn.setText(resources.getString(R.string.servo11))

                    5 -> btn.setText(resources.getString(R.string.servo12))
                    6 -> btn.setText(resources.getString(R.string.servo13))
                    7 -> btn.setText(resources.getString(R.string.servo14))
                    8 -> btn.setText(resources.getString(R.string.servo15))
                    else -> btn.setText(resources.getString(R.string.servo0))
                }

                dialog.dismiss()
            }

        // bind btn onclick
        btn.setOnClickListener {
            alertBuilder.show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibration)

        // 自动发送校准命令, 发送C指令的时候，会在底层自动清理全部的数据缓存
        // 所以这里在获取数据返回后，需要对数据进行清晰
        send("c")

        // bind widgets
        bindBackBtn()
        bindSaveBtn()
        bindFineAdjustBtn()
        bindTvOutput()
        bindSevorSelectionBtn()

        // 设置隐形输出，并启动监听
        setTextView(tvOutput)
        startCalibListening()
    }

    // 业务关闭
    override fun onDestroy() {
        super.onDestroy()
        stopCalibListening()
        Log.d("CalibrationActivity", "onDestroy")
    }

    // 监听返回键
    override fun onBackPressed() {
        super.onBackPressed()

        // 跳转回MainActivity
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}