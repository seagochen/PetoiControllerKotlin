package com.petoi.kotlin.android.app.bitmap

import android.graphics.Bitmap
import android.graphics.Matrix


open class ImageBitmapTool {

    fun scaleBitmap(origin: Bitmap?, scale: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val height = origin.height
        val width = origin.width
        val matrix = Matrix()
        matrix.postScale(scale, scale) // 使用后乘
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (!origin.isRecycled) {
            origin.recycle()
        }
        return newBM
    }

    fun transBitmap(origin: Bitmap?, x: Float, y: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val height = origin.height
        val width = origin.width
        val matrix = Matrix()
        matrix.setTranslate(x, y)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (!origin.isRecycled) {
            origin.recycle()
        }
        return newBM
    }

    fun rotateBitmap(origin: Bitmap?, r: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val height = origin.height
        val width = origin.width
        val matrix = Matrix()
        matrix.preRotate(r)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (!origin.isRecycled) {
            origin.recycle()
        }
        return newBM
    }
}