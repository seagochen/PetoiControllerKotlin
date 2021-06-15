package com.petoi.kotlin.android.app

import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import com.petoi.kotlin.android.app.widgets.MainPopupMenu

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : BluetoothBasedActivity() {

    // 绑定弹出菜单
    private fun bindPopupMenu() {
        val btn = findViewById<Button>(R.id.btn_main_menu)
        val menu = MainPopupMenu()
        btn.setOnClickListener {
            menu.showMenu(this, it)
        }
    }


    // Android Activity初始化后的调用函数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // 绑定Android控件和它们应该对应的消息事件
        bindPopupMenu()
    }
}