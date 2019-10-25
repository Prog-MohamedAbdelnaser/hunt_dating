package com.recep.hunt.utilis

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.widget.ImageView
import android.graphics.RectF
import android.util.AttributeSet


/**
 * Created by Rishabh Shukla
 * on 2019-08-27
 * Email : rishabh1450@gmail.com
 */

class RoundRectCornerImageView(val ctx: Context,val attrs: AttributeSet, val defStyle: Int): ImageView(ctx) {
    private val radius = 18.0f
    private var path: Path? = null
    private var rect: RectF? = null

    private fun init() {
        path = Path()

    }
    override fun onDraw(canvas: Canvas?) {
        rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
        path!!.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas!!.clipPath(path)
        super.onDraw(canvas)
    }
//    protected fun onDraw(canvas: Canvas) {
//        rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
//        path!!.addRoundRect(rect, radius, radius, Path.Direction.CW)
//        canvas.clipPath(path)
//        super.onDraw(canvas)
//    }
}