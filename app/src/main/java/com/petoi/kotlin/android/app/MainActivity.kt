package com.petoi.kotlin.android.app

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import com.petoi.kotlin.android.app.widgets.MainPopupMenu

enum class KineState {
    NORMAL, RUN, CRAWL, STOP
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : BluetoothBasedActivity() {

    private var kinestate: KineState = KineState.STOP

    // 绑定弹出菜单
    private fun bindPopupMenu() {
        val btn = findViewById<Button>(R.id.btn_main_menu)
        val menu = MainPopupMenu()
        btn.setOnClickListener {
            menu.showMenu(this, it)
        }
    }

    // 绑定方向键
    private fun bindArrows() {
        val up = findViewById<ImageButton>(R.id.btn_main_up)
        val down = findViewById<ImageButton>(R.id.btn_main_down)
        val left = findViewById<ImageButton>(R.id.btn_main_left)
        val right = findViewById<ImageButton>(R.id.btn_main_right)

        
        up.setOnTouchListener { v, event ->
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("MainActivity", "up pressed")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MainActivity", "up released")
                    v.performClick()
                }
            }
            return@setOnTouchListener true
        }


        down.setOnTouchListener { v, event ->
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("MainActivity", "down pressed")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MainActivity", "down released")
                    v.performClick()
                }
            }
            return@setOnTouchListener true
        }

        left.setOnTouchListener { v, event ->
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("MainActivity", "left pressed")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MainActivity", "left released")
                    v.performClick()
                }
            }
            return@setOnTouchListener true
        }

        right.setOnTouchListener { v, event ->
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("MainActivity", "right pressed")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MainActivity", "right released")
                    v.performClick()
                }
            }
            return@setOnTouchListener true
        }
    }



    // Android Activity初始化后的调用函数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // 绑定Android控件和它们应该对应的消息事件
        bindPopupMenu()
        bindArrows()
    }
}