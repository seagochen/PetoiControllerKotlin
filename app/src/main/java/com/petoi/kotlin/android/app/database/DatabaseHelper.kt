package com.petoi.kotlin.android.app.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception

open class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "petoi",null,1)  {

    private val petoi = PetoiInstruction()

    override fun onCreate(db: SQLiteDatabase?) {
        // 如果不存在则创建数据表
        db?.execSQL(petoi.createTable())

        // 准备插入预备数据
        val defaultIns: MutableList<ContentValues> = mutableListOf()

        // add instructions to list
        defaultIns.add(petoi.toInsertSql("Look Around", "kck"))
        defaultIns.add(petoi.toInsertSql("Stretch", "kstr"))
        defaultIns.add(petoi.toInsertSql("Greeting", "khi"))
        defaultIns.add(petoi.toInsertSql("Pee", "kpee"))
        defaultIns.add(petoi.toInsertSql("Push-up", "kpu"))

        // 写入数据库中
        for (values in defaultIns) {
            writableDatabase.insert(petoi.table_name, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 删除原有的数据库
        db?.execSQL(petoi.dropTable())

        // 如果不存在则创建数据表
        db?.execSQL(petoi.createTable())

        // 准备插入预备数据
        val defaultIns: MutableList<ContentValues> = mutableListOf()

        // add instructions to list
        defaultIns.add(petoi.toInsertSql("Look Around", "kck"))
        defaultIns.add(petoi.toInsertSql("Stretch", "kstr"))
        defaultIns.add(petoi.toInsertSql("Greeting", "khi"))
        defaultIns.add(petoi.toInsertSql("Pee", "kpee"))
        defaultIns.add(petoi.toInsertSql("Push-up", "kpu"))

        // 写入数据库中
        for (values in defaultIns) {
            writableDatabase.insert(petoi.table_name, null, values)
        }
    }

    // 插入数据
    fun insertInstruction(name: String, command: String): Pair<Boolean, String> {

        if (containKeys().contains(name)) {
            return Pair(false, "Cannot insert new key/value pair to database," +
                    " because duplicate name found")
        }

        val values = petoi.toInsertSql(name, command)
        writableDatabase.insert(petoi.table_name, null, values)
        return Pair(true, "OK")
    }

    // 查找数据
    fun searchInstruction(name: String): Pair<Boolean, String> {

        if (!containKeys().contains(name)) {
            return Pair(false, "Cannot found the given name, not exist or invalid name as input")
        }

        val projection =  arrayOf(petoi.column_id, petoi.column_name, petoi.column_ins)
        val selection = "${petoi.column_name} = ?"
        val selectionArgs = arrayOf(name)

        var instruction = ""

        // 查询可读数据库
        val cursor = readableDatabase.query(
            petoi.table_name,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null)

        // handle data
        try {
            // only deal with the first item
            if (cursor.moveToFirst()) {
                instruction = cursor.getString(cursor.getColumnIndex(petoi.column_ins))
            }
        } catch (e: Exception) {
            Log.d("DatabaseHelper", "searchInstruction failed", e)
        } finally {
            if(cursor != null && !cursor.isClosed){
                cursor.close()
            }
        }

        return Pair(true, instruction)
    }

    // 更新系统指令
    fun updateInstruction(name: String, command: String): Pair<Boolean, String> {
        if (!containKeys().contains(name)) {
            return Pair(false, "Cannot found the given name, not exist or invalid name as input")
        }

        val selection = "${petoi.column_name} LIKE ?"
        val selectionArgs = arrayOf(name)
        val values = petoi.toUpdateSql(petoi.column_ins, command)

        // handle data
        val count = writableDatabase.update(
            petoi.table_name,
            values,
            selection,
            selectionArgs
        )

        // return
        return if (count > 0) {
            Pair(true, "OK")
        } else {
            Pair(false, "failed, count is 0")
        }
    }

    // 删除指令
    fun deleteInstruction(name: String): Pair<Boolean, String> {
        if (!containKeys().contains(name)) {
            return Pair(false, "Cannot found the given name, not exist or invalid name as input")
        }

        val selection = "${petoi.column_name} LIKE ?"
        val selectionArgs = arrayOf(name)

        // handle data
        val count = writableDatabase.delete(
            petoi.table_name,
            selection,
            selectionArgs
        )

        // return
        return if (count > 0) {
            Pair(true, "OK")
        } else {
            Pair(false, "failed, count is 0")
        }
    }

    fun containKeys(): List<String> {

        val names = mutableListOf<String>()

        val projection =  arrayOf(petoi.column_name)

        // 查询可读数据库
        val cursor = readableDatabase.query(
            petoi.table_name,
            projection,
            null,
            null,
            null,
            null,
            null)

        // handle data
        try {
            // only deal with the first item
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex(petoi.column_name))
                    names.add(name)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.d("DatabaseHelper", "searchInstruction failed", e)
        } finally {
            if(cursor != null && !cursor.isClosed){
                cursor.close()
            }
        }

        // return
        return names
    }
}