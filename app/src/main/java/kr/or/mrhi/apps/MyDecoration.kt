package kr.or.mrhi.apps

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class MyDecoration(val context: Context): RecyclerView.ItemDecoration() {
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        val width = parent.width
        val height = parent.height
        val imageDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.img_background, null)
        val imageWidth = imageDrawable?.intrinsicWidth
        val imageHeight = imageDrawable?.intrinsicHeight
        val x = width / 2 - imageWidth?.div(2) as Int
        val y = height / 2 - imageHeight?.div(2) as Int

        canvas.drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.img_background), x.toFloat(), y.toFloat(), null)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(10, 10, 10, 10)

        view.setBackgroundColor(Color.parseColor("#BFFFFFFF"))
        ViewCompat.setElevation(view, 20.0f)
    }
}