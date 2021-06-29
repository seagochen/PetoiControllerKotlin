package com.petoi.kotlin.android.app.properties

import android.content.Context
import java.lang.Exception
import java.util.*

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
}