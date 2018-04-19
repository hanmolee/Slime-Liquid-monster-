package hanmo.com.slime.lockscreen.utils

import android.content.Context
import android.graphics.*
import android.view.View
import android.util.AttributeSet
import android.util.TypedValue
import hanmo.com.slime.R
import java.util.*


/**
 * Created by hanmo on 2018. 4. 18..
 */
class ClockView : View {

    private var clockHeight: Int = 0
    private var clockWidth = 0
    private var padding = 0
    private var fontSize = 0
    private val numeralSpacing = 0
    private var handTruncation: Int = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var paint: Paint? = null
    private var isInit: Boolean = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun initClock() {
        clockHeight = height
        clockWidth = width
        padding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f,
                resources.displayMetrics).toInt()
        val min = Math.min(clockHeight, clockWidth)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        paint = Paint()
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }

        drawCircle(canvas)
        drawNumeral(canvas)
        drawHands(canvas)

        postInvalidateDelayed(500)
    }

    private fun drawHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius = if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas.drawLine((clockWidth / 2).toFloat(), (clockHeight / 2).toFloat(),
                (clockWidth / 2 + Math.cos(angle) * handRadius).toFloat(),
                (clockHeight / 2 + Math.sin(angle) * handRadius).toFloat(),
                paint!!)
    }

    private fun drawHands(canvas: Canvas) {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, ((hour + c.get(Calendar.MINUTE) / 60) * 5f).toDouble(), true)
        drawHand(canvas, c.get(Calendar.MINUTE).toDouble(), false)
    }

    private fun drawNumeral(canvas: Canvas) {
        paint!!.textSize = fontSize.toFloat()

        for (number in numbers) {
            val tmp = number.toString()
            paint!!.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (clockWidth / 2 + Math.cos(angle) * radius - rect.width() / 2).toInt()
            val y = ((clockHeight / 2).toDouble() + Math.sin(angle) * radius + (rect.height() / 2).toDouble()).toInt()

            val image = BitmapFactory.decodeResource(context.resources, R.drawable.pic_hour)

            canvas.drawBitmap(image, x.toFloat(), y.toFloat(), paint!!)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        paint!!.reset()
        paint!!.color = resources.getColor(android.R.color.white)
        paint!!.strokeWidth = 5f
        paint!!.style = Paint.Style.STROKE
        paint!!.isAntiAlias = true
        canvas.drawCircle((clockWidth / 2).toFloat(), (clockHeight / 2).toFloat(), (radius + padding - 10).toFloat(), paint!!)
    }

}