package com.petoi.kotlin.android.app.properties

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

enum class PetoiDevice {
    Bittle,
    Nybble
}

class PetoiProperties {

    companion object {
        fun getProperties(context: Context): Properties {
            val activityProperties = Properties()
            try {
                val assets = context.assets.open("activityConfig")
                activityProperties.load(assets)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return activityProperties
            }
        }
    }

    fun isFirstTime(context: Context): Boolean {
        val properties = getProperties(context)
        if (properties.containsKey("first_time")) {
            return false
        }
        return true
    }

    fun setFirstTime(context: Context) {
        val properties = getProperties(context)
        val currentTime = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        properties.setProperty("first_time", currentTime.format(Date()))
    }

    fun getSelectedPetoiDevice(context: Context): PetoiDevice {
        val properties = getProperties(context)
        val currentDevice =  properties.get("petoi_device") as PetoiDevice
        return currentDevice
    }

    fun setSelectedPetoiDevice(context: Context, device: PetoiDevice) {
        val properties = getProperties(context)
        properties.set("petoi_device", device)
    }
}