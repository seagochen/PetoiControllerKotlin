package com.petoi.kotlin.android.app.calibration

import android.util.Log

open class UiCalibVerification {
    private val servos = mutableListOf<Pair<String, Int>>()

    init {
        // head
        servos.add(Pair<String, Int>("0", 0));

        // upper arm and leg
        servos.add(Pair<String, Int>("8", 0));
        servos.add(Pair<String, Int>("9", 0));
        servos.add(Pair<String, Int>("10", 0));
        servos.add(Pair<String, Int>("11", 0));

        // lower arm and leg
        servos.add(Pair<String, Int>("12", 0));
        servos.add(Pair<String, Int>("13", 0));
        servos.add(Pair<String, Int>("14", 0));
        servos.add(Pair<String, Int>("15", 0));
    }

    fun count(): Int { return servos.size }

    fun angle(pos: Int): Int { return servos[pos].second }

    fun name(pos: Int): String { return servos[pos].first }

    fun increaseCalibAngle(pos: Int): Boolean {
        var current_angle = servos[pos].second
        val name = servos[pos].first

        // increase one degree
        current_angle += 1
        if (current_angle > 9) {
            return false;
        }

        // send updated-angle
//        QString updated = QString("c%1 %2").arg(name).arg(current_angle);
//        MainWindow::uiSerialHandler.sendCmdViaSerialPort(updated);
        //TODO 将修改好的值发送给设备

        // update record
        servos[pos] = servos[pos].copy(second = current_angle)
        return true
    }

    fun decreaseCalibAngle(pos: Int): Boolean {
        var current_angle = servos[pos].second
        val name = servos[pos].first

        // increase one degree
        current_angle -= 1
        if (current_angle < -9) {
            return false;
        }

        // send updated-angle
//        QString updated = QString("c%1 %2").arg(name).arg(current_angle);
//        MainWindow::uiSerialHandler.sendCmdViaSerialPort(updated);
        //TODO 将修改好的值发送给设备

        // update record
        servos[pos] = servos[pos].copy(second = current_angle)
        return true
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
            val servos_list = fb.split(",");

            // check digital strings length
            if (servos_list.count() < 16) {
                Log.i("UiCalibVerification", "Error calibration list: $fb")
                return false;
            }

            // input value check
            for (i in 0 until 16) {

                Log.i("UiCalibVerification", "check value: $servos_list[i]")
                if (!calib.isValidAngleDegree(servos_list[i])) {
                    Log.i("UiCalibVerification", "failed")
                    return false;
                }
            }

            //TODO 赋值，以后如果有需要可能会扩展舵机数
            servos[0] = servos[0].copy(second = servos_list[0].toInt())

            servos[1] = servos[1].copy(second = servos_list[1].toInt())
            servos[2] = servos[2].copy(second = servos_list[2].toInt())
            servos[3] = servos[3].copy(second = servos_list[3].toInt())
            servos[4] = servos[4].copy(second = servos_list[4].toInt())

            servos[5] = servos[5].copy(second = servos_list[12].toInt())
            servos[6] = servos[6].copy(second = servos_list[13].toInt())
            servos[7] = servos[7].copy(second = servos_list[14].toInt())
            servos[8] = servos[8].copy(second = servos_list[15].toInt())

            return true
        }

        return false
    }

    fun clearCalibAngle(pos: Int) {
        val name = servos[pos].first;

        // send updated-angle
//        QString updated = QString("c%1 %2").arg(name).arg(0);
//        MainWindow::uiSerialHandler.sendCmdViaSerialPort(updated);
        //TODO 将修改好的值发送给设备

        // update record
        servos[pos] = servos[pos].copy(second = 0)
    }
}