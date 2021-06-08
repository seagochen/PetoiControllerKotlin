package com.petoi.kotlin.android.app.calibration

import android.util.Log

open class UiCalibVerification {
    private val servos = mutableListOf<Pair<String, Int>>()

    init {
        // head
        servos.add(Pair("0", 0))

        // upper arm and leg
        servos.add(Pair("8", 0))
        servos.add(Pair("9", 0))
        servos.add(Pair("10", 0))
        servos.add(Pair("11", 0))

        // lower arm and leg
        servos.add(Pair("12", 0))
        servos.add(Pair("13", 0))
        servos.add(Pair("14", 0))
        servos.add(Pair("15", 0))
    }

    fun count(): Int { return servos.size }

    fun angle(pos: Int): Int { return servos[pos].second }

    fun name(pos: Int): String { return servos[pos].first }

    fun increaseCalibAngle(pos: Int): Pair<Boolean, String> {
        var currentAngle = servos[pos].second
        val name = servos[pos].first

        // increase one degree
        currentAngle += 1
        if (currentAngle > 9) {
            return Pair(false, "")
        }

        // update record
        servos[pos] = servos[pos].copy(second = currentAngle)

        // 调整指令
        val cmd = "c$name $currentAngle"
        return Pair(true, cmd)
    }

    fun decreaseCalibAngle(pos: Int): Pair<Boolean, String> {
        var currentAngle = servos[pos].second
        val name = servos[pos].first

        // increase one degree
        currentAngle -= 1
        if (currentAngle < -9) {
            return Pair(false, "")
        }

        // update record
        servos[pos] = servos[pos].copy(second = currentAngle)

        // 调整指令
        val cmd = "c$name $currentAngle"
        return Pair(true, cmd)
    }

    fun updateCalibrationInfo(feedback: String): Boolean {
        val calib = CalibFeedback()
        var fb = calib.preprocessCalibString(feedback)
        fb = calib.distillValidCalibString(fb)

        // 打印处理后的校准数据
        Log.i("UiCalibVerification", "calibration vals: $fb")

        // 把校准数据转换成角度Int
        if (fb != "") { // found valid data

            // split the string into lists
            val servosList = fb.split(",");

            // check digital strings length
            if (servosList.count() < 16) {
                Log.i("UiCalibVerification", "Error calibration list: $fb")
                return false;
            }

            // input value check
            for (i in 0 until 16) {

                Log.i("UiCalibVerification", "check value: $servosList[i]")
                if (!calib.isValidAngleDegree(servosList[i])) {
                    Log.i("UiCalibVerification", "failed")
                    return false
                }
            }

            //TODO 赋值，以后如果有需要可能会扩展舵机数
            servos[0] = servos[0].copy(second = servosList[0].toInt())

            servos[1] = servos[1].copy(second = servosList[1].toInt())
            servos[2] = servos[2].copy(second = servosList[2].toInt())
            servos[3] = servos[3].copy(second = servosList[3].toInt())
            servos[4] = servos[4].copy(second = servosList[4].toInt())

            servos[5] = servos[5].copy(second = servosList[12].toInt())
            servos[6] = servos[6].copy(second = servosList[13].toInt())
            servos[7] = servos[7].copy(second = servosList[14].toInt())
            servos[8] = servos[8].copy(second = servosList[15].toInt())

            return true
        }

        return false
    }

    fun clearCalibAngle(pos: Int): Pair<Boolean, String> {
        val name = servos[pos].first

        // update record
        servos[pos] = servos[pos].copy(second = 0)

        // 调整指令
        val cmd = "c$name 0"
        return Pair<Boolean, String>(true, cmd)
    }
}