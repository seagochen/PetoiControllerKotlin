package com.petoi.kotlin.android.app.widgets

import android.view.View
import android.widget.ImageButton
import com.petoi.kotlin.android.app.R

open class MotionItemViewWrapper(base: View) {
    private var view: View
    private var editBtn: ImageButton

    init {
        view = base
        editBtn = view.findViewById(R.id.btn_main_motion_list_edit)
    }

    fun getEditButton(): ImageButton {
        return editBtn
    }
}