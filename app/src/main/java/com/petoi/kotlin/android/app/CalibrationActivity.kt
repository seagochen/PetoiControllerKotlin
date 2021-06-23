package com.petoi.kotlin.android.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import com.petoi.kotlin.android.app.calibration.CalibrationVerification

class CalibrationActivity : BluetoothBasedActivity() {

    // 从Application获得校准处理模块
    private val calib = ApplicationData.calibration

    // textview用于数据缓存处理
    private lateinit var tvOutput: TextView

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