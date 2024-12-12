package com.pl.sages.medical.watermepro.view.profile

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.squareup.picasso.Transformation


class GrayscaleTransformation : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        val saturation = ColorMatrix()
        saturation.setSaturation(0f)

        val paint = Paint()
        paint.setColorFilter(ColorMatrixColorFilter(saturation))

        canvas.drawBitmap(source, 0f, 0f, paint)
        source.recycle()

        return bitmap
    }

    override fun key(): String {
        return "GrayscaleTransformation()"
    }
}