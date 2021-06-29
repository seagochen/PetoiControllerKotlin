package com.petoi.kotlin.android.app.properties

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

enum class PetoiDevice(val value: Int) {
    Bittle(0), Nybble(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}

class PetoiPreferences(context: Context) {

    val preferences: SharedPreferences = context.getSharedPreferences("configs", Context.MODE_PRIVATE)


    fun isFirstTime(context: Context): Boolean {
        val date = preferences.getString("first_time", "")
        if (date != "") {
            return false
        }
        return true
    }

    fun setFirstTime(context: Context) {
        val editor = preferences.edit()
        val currentTime = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        editor.putString("first_time", currentTime.format(Date()))
        editor.apply()
    }

    fun getSelectedPetoiDevice(context: Context): PetoiDevice {
        return PetoiDevice.fromInt(preferences.getInt("petoi_device", PetoiDevice.Bittle.ordinal))
    }

    fun setSelectedPetoiDevice(context: Context, device: PetoiDevice) {
        val editor = preferences.edit()
        editor.putInt("petoi_device", device.ordinal)
        editor.apply()
    }
}