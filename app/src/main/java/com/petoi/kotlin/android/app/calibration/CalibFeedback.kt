package com.petoi.kotlin.android.app.calibration

import android.util.Log


open class CalibFeedback {

    private fun distillOnceMore(calib: String): String {
        val pattern = "(([-\\+]?\\d),\\s)+"
        val reg = Regex(pattern)
        val result: String? = reg.find(calib)?.value

        if (result != null) {
            return result.replace(" ", "").replace(", ", ",")
        }

        Log.i("CalibFeedbackProcedure", "distillOnceMore failed: $calib")
        return ""
    }


    fun distillValidCalibString(calib: String): String {
        val pattern = "(0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15)\\s+(([-\\+]?\\d),\\s)+"
        val reg = Regex(pattern)
        val result: String? = reg.find(calib)?.value

        if (result != null) {
            return distillOnceMore(result)
        }

        Log.i("CalibFeedbackProcedure", "distillValidCalibString failed: $calib")
        return ""
    }

    fun preprocessCalibString(calib: String): String {
        val newCalib = calib.replace("\r", " ")
            .replace("\n", " ")
            .replace("\t", " ")

        return newCalib
    }

    fun isValidAngleDegree(angle: String): Boolean {
        val pattern = "^([-|+]?\\d+)$"
        val reg = Regex(pattern)

        return reg.matches(angle);
    }
}