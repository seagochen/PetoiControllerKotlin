package com.petoi.kotlin.android.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import com.petoi.kotlin.android.app.database.DatabaseHelper

class EditorActivity : BluetoothBasedActivity() {

    // 动作数据库
    private val sqliteDB = DatabaseHelper(this)

    // 用于输出测试结果
    private lateinit var tvOutput: TextView

    //
    private lateinit var txActionName: EditText
    private lateinit var txActionCmd: EditText

    // 指令类型
    private var position = -1

    // 指令名
    private var name = ""

    // 指令
    private var command = ""

    private fun bindEditTextes() {
        txActionName = findViewById(R.id.tx_editor_name)
        txActionCmd = findViewById(R.id.tx_editor_command)
    }

    private fun bindBackBtn() {
        val btn = findViewById<Button>(R.id.btn_motion_back)
        btn.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun bindTestBtn() {
        val btn = findViewById<Button>(R.id.btn_editor_test)

        btn.setOnClickListener {
            if (txActionName.text.isEmpty() || txActionName.text.isBlank()) {
                Toast.makeText(this@EditorActivity,
                    "User defined command should have a name!",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (txActionCmd.text.isEmpty() || txActionCmd.text.isBlank()) {
                Toast.makeText(this@EditorActivity,
                    "User defined command shouldn't left as blank!",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // send command
            val str = txActionCmd.text.toString()
            send(str)
        }
    }

    private fun bindSaveBtn() {
        val btn = findViewById<Button>(R.id.btn_editor_save)

        btn.setOnClickListener {

            // 获取存储的全部数据
            name = txActionName.text.toString()
            command = txActionCmd.text.toString()

            if (position == -1) { // 新增指令
                val ret = sqliteDB.insertInstruction(name, command)

                if (!ret.first) {
                    Toast.makeText(this@EditorActivity, ret.second, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else { // 修改指令
                val ret = sqliteDB.updateInstruction(name, command)

                if (!ret.first) {
                    Toast.makeText(this@EditorActivity, ret.second, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun bindTvOutput() {
        tvOutput = findViewById(R.id.tv_editor_output)
        setTextView(tvOutput)
        startCalibListening()
    }

    private fun getExtraData() {
        val extras = intent.extras
        if (extras != null) {
            position = extras.getInt("position")
            name = extras.getString("cmdName").toString()
            command = extras.getString("cmdDetail").toString()
        }

        if (position == -1) { // 新建动作
            txActionName.isEnabled = true
            txActionName.setText("")
            txActionCmd.setText("")
        } else { // 动作修改
            txActionName.setText(name)
            txActionCmd.setText(command)
            txActionName.isEnabled = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        // bind widgets
        bindEditTextes()
        bindBackBtn()
        bindTestBtn()
        bindSaveBtn()
        bindTvOutput()

        // get data
        getExtraData()
    }

    // 业务关闭
    override fun onDestroy() {
        super.onDestroy()
        stopCalibListening()
        Log.d("EditorActivity", "onDestroy")
    }

    // 主后退键
    override fun onBackPressed() {
        super.onBackPressed()

        // 跳转回MainActivity
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}