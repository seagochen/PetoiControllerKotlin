package com.petoi.kotlin.android.app.widgets

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.petoi.kotlin.android.app.R


open class MainPopupMenu : PopupMenu.OnMenuItemClickListener {

    fun showMenu(context: Context, v: View) {
        val popup = PopupMenu(context, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.menu_main)
        popup.show()
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.menu_main_calib -> {            // do your code
                Log.d("MainDropMenu", "menu_main_calib")
                // TODO
                true
            }

            R.id.menu_main_help -> {
                Log.d("MainDropMenu", "menu_main_help")
                // TODO
                true
            }

            else -> false
        }
    }
}


