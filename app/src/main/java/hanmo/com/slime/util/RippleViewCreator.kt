package hanmo.com.slime.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.NonNull
import android.view.ViewGroup
import android.view.MotionEvent
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import hanmo.com.slime.R


/**
 * Created by hanmo on 2018. 4. 19..
 */
class RippleViewCreator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val duration = 150f
    private val frameRate = 15

    private var speed = 1f
    private var radius = 0f
    private val paint = Paint()
    private var endRadius = 0f
    private var rippleX = 0f
    private var rippleY = 0f
    private var drawWidth = 0
    private var drawHeight = 0
    private var touchAction: Int = 0

    init {
        init()
    }

    private fun init() {
        if (isInEditMode)
            return

        paint.setStyle(Paint.Style.FILL)
        paint.setColor(resources.getColor(R.color.abc_color_highlight_material))
        paint.setAntiAlias(true)

        setWillNotDraw(true)
        isDrawingCacheEnabled = true
        isClickable = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawWidth = w
        drawHeight = h
    }

    override fun dispatchDraw(@NonNull canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (radius > 0 && radius < endRadius) {
            canvas.drawCircle(rippleX, rippleY, radius, paint)
            if (touchAction == MotionEvent.ACTION_UP)
                invalidate()
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return true
    }

    override fun onTouchEvent(@NonNull event: MotionEvent): Boolean {
        rippleX = event.x
        rippleY = event.y

        touchAction = event.action

        when (event.action) {
            MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(false)

                radius = 1f
                endRadius = Math.max(Math.max(Math.max(drawWidth - rippleX, rippleX), rippleY), drawHeight - rippleY)
                speed = endRadius / duration * frameRate
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        if (radius < endRadius) {
                            radius += speed
                            paint.setAlpha(90 - (radius / endRadius * 90).toInt())
                            handler.postDelayed(this, frameRate.toLong())
                        } else if (getChildAt(0) != null) {
                            getChildAt(0).performClick()
                        }
                    }
                }, frameRate.toLong())
            }
            MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                endRadius = Math.max(Math.max(Math.max(drawWidth - rippleX, rippleX), rippleY), drawHeight - rippleY)
                paint.setAlpha(90)
                radius = endRadius / 3
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (rippleX < 0 || rippleX > drawWidth || rippleY < 0 || rippleY > drawHeight) {
                    parent.requestDisallowInterceptTouchEvent(false)
                    touchAction = MotionEvent.ACTION_CANCEL
                    
                } else {
                    invalidate()
                    return true
                }
            }
        }
        invalidate()
        return false
    }

    override fun addView(@NonNull child: View, index: Int, params: ViewGroup.LayoutParams) {
        //limit one view
        if (childCount > 0) {
            throw IllegalStateException(this.javaClass.toString() + " can only have one child.")
        }
        super.addView(child, index, params)
    }

    companion object {

        fun addRippleToView(v: View) {
            val parent = v.getParent() as ViewGroup
            var index = -1
            if (parent != null) {
                index = parent.indexOfChild(v)
                parent.removeView(v)
            }
            val rippleViewCreator = RippleViewCreator(v.getContext())
            rippleViewCreator.layoutParams = v.getLayoutParams()
            if (index == -1)
                parent.addView(rippleViewCreator, index)
            else
                parent.addView(rippleViewCreator)
            rippleViewCreator.addView(v)
        }
    }
}