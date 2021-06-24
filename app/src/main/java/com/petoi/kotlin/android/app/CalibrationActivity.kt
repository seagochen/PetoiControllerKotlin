package com.petoi.kotlin.android.app

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.petoi.kotlin.android.app.bitmap.ImageBitmapTool
import com.petoi.kotlin.android.app.bluetooth.BluetoothBasedActivity


class CalibrationActivity : BluetoothBasedActivity() {

    // 从Application获得校准处理模块
    private val calib = ApplicationData.calibration

    // textview用于数据缓存处理
    private lateinit var tvOutput: TextView

    // 选中的舵机
    private var selectedServo = 0

    private var imgTool = ImageBitmapTool()

    // 绑定textview
    private fun bindTvOutput() {
        tvOutput = findViewById<TextView>(R.id.tv_calib_output)
        setTextView(tvOutput)
        startCalibListening()
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
                return@setOnClickListener
            }

            send(ret.second)
            updateServosInfoOnImage()
        }

        // fine subtraction
        desBtn.setOnClickListener {
            val ret = calib.decreaseCalibAngle(selectedServo)

            if (!ret.first) {
                alertBuilder.show()
                return@setOnClickListener
            }

            send(ret.second)
            updateServosInfoOnImage()
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

                updateServosInfoOnImage()
                dialog.dismiss()
            }

        // bind btn onclick
        btn.setOnClickListener {
            alertBuilder.show()
        }
    }

    private fun updateServosInfoOnImage() {
        // display matric
        val img = BitmapFactory.decodeResource(resources, R.drawable.illustration)
//        val metrics = getResources().displayMetrics
        val main = findViewById<ImageView>(R.id.img_calib_main)

        // debug
//        Log.d("CalibrationActivity", "screen size: $metrics")

        // 创建bitmap copy并同时创建canvas
//        val bitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888)
        val bitmap = Bitmap.createBitmap(main.width, main.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 绘制参数设置
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.DKGRAY
        paint.textSize = 30F

        // 绘制文本信息
        var increase_y = 150F
        for (i in 0..calib.count() - 1) {
            canvas.drawText("Servo #${calib.name(i)} ...${calib.angle(i)}",
                5F, increase_y, paint)
            increase_y += 35F
        }

        // 绘制图片
        val scale = main.height.toFloat() / img.height.toFloat()
        val outImg = imgTool.scaleBitmap(img, scale)


        if (outImg != null) {
            val transx = img.width / 2
            canvas.drawBitmap(outImg, transx.toFloat(), 0F, paint)
        }

        // update image
        main.setImageBitmap(bitmap)
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
        bindSevorSelectionBtn()
        bindTvOutput()
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