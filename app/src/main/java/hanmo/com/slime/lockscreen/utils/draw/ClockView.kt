package hanmo.com.slime.lockscreen.utils.draw

import android.content.Context
import android.graphics.*
import android.view.View
import android.util.AttributeSet
import android.util.TypedValue
import hanmo.com.slime.R
import hanmo.com.slime.util.DLog
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
    private lateinit var paint: Paint
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

    private fun drawHourHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius = if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation

        val hourPaint = Paint()
        with(hourPaint) {
            strokeWidth = 13f
            style = Paint.Style.STROKE
            color = resources.getColor(android.R.color.white)
            isDither = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            pathEffect = CornerPathEffect(10f)
            isAntiAlias = true
        }

        canvas.drawLine((clockWidth / 2 - Math.cos(angle) * 10).toFloat(), (clockWidth / 2 - Math.sin(angle) * 10).toFloat(),
                (clockWidth / 2 + Math.cos(angle) * handRadius).toFloat(),
                (clockHeight / 2 + Math.sin(angle) * handRadius).toFloat(),
                hourPaint)

        DLog.e("Hour")
    }

    private fun drawMinHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius = if (isHour) radius - handTruncation - hourHandTruncation else radius + padding - 10

        val minPaint = Paint()
        with(minPaint) {
            strokeWidth = 3f
            style = Paint.Style.STROKE
            color = resources.getColor(android.R.color.white)
            isDither = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            pathEffect = CornerPathEffect(10f)
            isAntiAlias = true
        }

        val sp = (clockWidth / 2).toFloat().toString()
        DLog.e(sp + ","+ sp + "/////" + (clockWidth / 2 + Math.cos(angle) * handRadius).toFloat().toString() + ", " + (clockHeight / 2 + Math.sin(angle) * handRadius).toFloat().toString())
        canvas.drawLine((clockWidth / 2 - Math.cos(angle) * 20).toFloat(), (clockWidth / 2 - Math.sin(angle) * 20).toFloat(),
                (clockWidth / 2 + Math.cos(angle) * handRadius).toFloat(),
                (clockHeight / 2 + Math.sin(angle) * handRadius).toFloat(),
                minPaint)
    }

    private fun drawHands(canvas: Canvas) {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        drawHourHand(canvas, ((hour + c.get(Calendar.MINUTE) / 60) * 5f).toDouble(), false)
        drawMinHand(canvas, c.get(Calendar.MINUTE).toDouble(), false)
    }

    private fun drawNumeral(canvas: Canvas) {

        val dialPaint1 = Paint()
        with(dialPaint1) {
            color = resources.getColor(android.R.color.white)
            strokeWidth = 1.7f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val dialPaint2 = Paint()
        with(dialPaint2) {
            color = resources.getColor(android.R.color.white)
            strokeWidth = 2.5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        for (number in numbers) {
            val tmp = number.toString()
            val ddd = radius  + padding - 10
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (clockWidth / 2 + Math.cos(angle) * ddd).toFloat()
            val y = (clockHeight / 2 + Math.sin(angle) * ddd).toFloat()

            when(number % 3) {
                0 -> { canvas.drawLine(x, y, (x - Math.cos(angle) * 16).toFloat(), (y - Math.sin(angle) * 16).toFloat(), dialPaint2) }
                else -> { canvas.drawLine(x, y, (x - Math.cos(angle) * 12).toFloat(), (y - Math.sin(angle) * 12).toFloat(), dialPaint1) }
            }

        }
    }

    private fun drawCircle(canvas: Canvas) {

        with(paint) {
            reset()
            color = resources.getColor(android.R.color.white)
            strokeWidth = 0.5f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        canvas.drawCircle((clockWidth / 2).toFloat(), (clockHeight / 2).toFloat(), (radius + padding - 10).toFloat(), paint)
    }

}