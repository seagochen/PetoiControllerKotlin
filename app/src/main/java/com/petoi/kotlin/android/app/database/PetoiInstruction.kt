package com.petoi.kotlin.android.app.database

import android.content.ContentValues
import java.text.SimpleDateFormat
import java.util.*

open class PetoiInstruction {

    val table_name = "instruction"
    val column_id = "id"
    val column_name = "name"
    val column_ins = "instruction"

    fun createTable(): String {
        val sentence = "CREATE TABLE IF NOT EXISTS $table_name (" +
                "$column_id TEXT PRIMARY KEY, " +
                "$column_name TEXT NOT NULL, " +
                "$column_ins TEXT NOT NULL)"
        return sentence
    }

    fun dropTable(): String {
        val sentence = "DROP TABLE IF EXISTS $table_name"
        return sentence
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        return sdf.format(Date())
    }


    fun toInsertSql(name: String, ins: String): ContentValues {
        val values = ContentValues()

        // append values to content values
        values.put(column_name, name)
        values.put(column_ins, ins)
        values.put(column_id, getCurrentDate())

        return values
    }

    fun toUpdateSql(key: String, value: String): ContentValues {
        val values = ContentValues().apply {
            put(key, value)
        }

        return values
    }
}