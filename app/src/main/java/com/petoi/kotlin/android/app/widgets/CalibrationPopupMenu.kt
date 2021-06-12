package com.petoi.kotlin.android.app.widgets

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.petoi.kotlin.android.app.R

class CalibrationPopupMenu : PopupMenu.OnMenuItemClickListener {

    fun showMenu(context: Context, v: View) {
        val popup = PopupMenu(context, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.menu_calibration)
        popup.show()
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.servo0 -> {            // do your code
                Log.d("CalibrationPopupMenu", "0")
                // TODO
                true
            }

            //TODO
            else -> false
        }
    }
}