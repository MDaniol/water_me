package com.pl.sages.medical.watermepro.view.profile

import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import kotlin.math.min


class CustomTransformation {
    class CropSquareTransformation : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size =
                min(source.width.toDouble(), source.height.toDouble()).toInt()
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2
            val result = Bitmap.createBitmap(source, x, y, size, size)
            if (result != source) {
                source.recycle()
            }
            return result
        }

        override fun key(): String {
            return "square()"
        }
    }
}