package com.petoi.kotlin.android.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import com.petoi.kotlin.android.app.calibration.CalibrationVerification

class CalibrationActivity : BluetoothBasedActivity() {

    private val calibration = CalibrationVerification()


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
    }
}