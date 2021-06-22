package com.petoi.kotlin.android.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity
import com.petoi.kotlin.android.app.database.DatabaseHelper
import com.petoi.kotlin.android.app.widgets.MainPopupMenu
import com.petoi.kotlin.android.app.widgets.MotionEditorAdapter

enum class KineState {
    NORMAL, RUN, CRAWL
}

enum class MotionItemState {
    EDIT, DELETE
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : BluetoothBasedActivity() {

    private var kinestate: KineState = KineState.NORMAL
    private var itemState: MotionItemState = MotionItemState.EDIT
    private val sqliteDB = DatabaseHelper(this)

    // 绑定弹出菜单
    private fun bindPopupMenu() {
        val btn = findViewById<Button>(R.id.btn_main_menu)
        val menu = MainPopupMenu()
        btn.setOnClickListener {
            menu.showMenu(this, it)
        }
    }

    // 绑定方向键
    @SuppressLint("ClickableViewAccessibility")
    private fun bindMotionDir() {
        val up = findViewById<ImageButton>(R.id.btn_main_up)
        val down = findViewById<ImageButton>(R.id.btn_main_down)
        val left = findViewById<ImageButton>(R.id.btn_main_left)
        val right = findViewById<ImageButton>(R.id.btn_main_right)

        // 前
        up.setOnTouchListener { v, event ->
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("MainActivity", "up pressed")

                    // 执行指令
                    when(kinestate) {
                        KineState.CRAWL-> {
                            send("kcrF")
                        }

                        KineState.RUN -> {
                            send("ktrF")
                        }

                        else -> {
                            send("kwkF")
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MainActivity", "up released")
                    v.performClick()

                    // 执行指令
                    send("kbalance")
                }
            }
            return@setOnTouchListener true
        }

        // 后
        down.setOnTouchListener { v, event ->
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("MainActivity", "down pressed")

                    // 执行指令
                    send("kbk")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MainActivity", "down released")
                    v.performClick()

                    // 执行指令
                    send("kbalance")
                }
            }
            return@setOnTouchListener true
        }

        // 左
        left.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("MainActivity", "left pressed")

                    // 执行指令
                    when(kinestate) {
                        KineState.CRAWL-> {
                            send("kcrL")
                        }

                        KineState.RUN -> {
                            send("ktrL")
                        }

                        else -> {
                            send("kwkL")
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MainActivity", "left released")
                    v.performClick()

                    // 执行指令
                    send("kbalance")
                }
            }
            return@setOnTouchListener true
        }

        // 右
        right.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("MainActivity", "right pressed")

                    // 执行指令
                    when(kinestate) {
                        KineState.CRAWL-> {
                            send("kcrR")
                        }

                        KineState.RUN -> {
                            send("ktrR")
                        }

                        else -> {
                            send("kwkR")
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MainActivity", "right released")
                    v.performClick()

                    // 执行指令
                    send("kbalance")
                }
            }
            return@setOnTouchListener true
        }
    }

    // 绑定运动状态
    private fun bindKineState() {
        val crawl = findViewById<Button>(R.id.btn_main_high_velocity)
        val run = findViewById<Button>(R.id.btn_main_medium_velocity)
        val normal = findViewById<Button>(R.id.btn_main_low_velocity)

        crawl.setOnClickListener {
            kinestate = KineState.CRAWL
        }

        run.setOnClickListener {
            kinestate = KineState.RUN
        }

        normal.setOnClickListener {
            kinestate = KineState.NORMAL
        }
    }

    // 绑定动作增删按钮
    private fun bindMotionItemButton() {
        val btnAdd = findViewById<ImageButton>(R.id.btn_main_add)
        val btnRmv = findViewById<ImageButton>(R.id.btn_main_remove)

        btnAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, MotionsEditActivity::class.java)
            intent.putExtra("position", -1)
            intent.putExtra("cmdName", "")
            intent.putExtra("cmdDetail", "")
            startActivity(intent)
        }
        btnRmv.setOnClickListener {
            itemState = MotionItemState.DELETE
        }
    }

    private fun bindMotionItemList() {
        val motionList = findViewById<ListView>(R.id.list_main_motions)

        // 从数据库中读取全部数据
        val items = sqliteDB.items()
        if (items.size > 0) {
            val editAdapter = MotionEditorAdapter(this@MainActivity, items)
            motionList.adapter = editAdapter

        }
    }

    // Android Activity初始化后的调用函数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // 绑定Android控件和它们应该对应的消息事件
        bindPopupMenu()
        bindMotionDir()
        bindKineState()
        bindMotionItemButton()
        bindMotionItemList()
    }
}