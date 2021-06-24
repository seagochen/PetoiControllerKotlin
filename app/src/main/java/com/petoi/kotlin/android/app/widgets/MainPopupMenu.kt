package com.petoi.kotlin.android.app.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.petoi.kotlin.android.app.CalibrationActivity
import com.petoi.kotlin.android.app.ConnectionActivity
import com.petoi.kotlin.android.app.R


open class MainPopupMenu : PopupMenu.OnMenuItemClickListener {

    var context: Context? = null

    fun showMenu(context: Context, v: View) {
        this.context = context

        val popup = PopupMenu(context, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.menu_main)
        popup.show()
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.menu_main_calib -> {            // do your code
                Log.d("MainDropMenu", "menu_main_calib")

                // 跳转到校准界面
                val intent = Intent(context, CalibrationActivity::class.java)
                context?.startActivity(intent)
                (context as Activity).finish()
                true
            }

            R.id.menu_main_connector -> {
                Log.d("MainDropMenu", "menu_main_connector")

                // 跳转到校准界面
                val intent = Intent(context, ConnectionActivity::class.java)
                context?.startActivity(intent)
                (context as Activity).finish()
                true
            }

            else -> false
        }
    }
}


