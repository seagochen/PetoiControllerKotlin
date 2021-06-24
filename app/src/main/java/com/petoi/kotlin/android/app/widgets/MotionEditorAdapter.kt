package com.petoi.kotlin.android.app.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.petoi.kotlin.android.app.EditorActivity
import com.petoi.kotlin.android.app.R

open class MotionEditorAdapter(
    context: Context,
    data: List<Pair<String, String>>): BaseAdapter() {

    private var commands: MutableList<Pair<String, String>>
    private var context: Context
    private var layoutInflater: LayoutInflater

    // 构造函数初始化数据
    init {
        this.context = context
        commands = data as MutableList<Pair<String, String>>
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return commands.size
    }

    override fun getItem(position: Int): Any {
        return commands[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        // 获得数据的布局
        var row = convertView
        val wrapper:MotionItemViewWrapper?

        if (row == null) {
            row = layoutInflater.inflate(R.layout.layout_main_motionlist, parent, false)
            wrapper = MotionItemViewWrapper(row)
        } else {
            wrapper = row.getTag() as MotionItemViewWrapper?
        }

        // 获得对应的指令数据, 并修改其中的数据
        val itemName = commands[position].first
        if (row != null) {
            val tvItemName = row.findViewById<TextView>(R.id.tv_main_motion_list_text)
            tvItemName.setText(itemName)
        }

        // 对按键设置事件响应
        if (wrapper != null) {
            wrapper.getEditButton().setOnClickListener {
                val intent = Intent(context, EditorActivity::class.java)
                intent.putExtra("position", position)
                intent.putExtra("cmdName", commands[position].first)
                intent.putExtra("cmdDetail", commands[position].second)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
        return row
    }

    fun remove(position: Int): String {
        if (commands.size > 0 && position >= 5) {
            val item = commands.removeAt(position)
            notifyDataSetChanged()
            return item.first
        }

        return ""
    }

    fun instruction(position: Int): String {
        if (commands.size > 0) {
            return commands[position].second
        }
        return ""
    }
}