package huaan.com.statisticsdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

fun View.dp2px(dp: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun View.px2dp(px: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}
/**
 * actor 晴天 create 2019/6/24
 */
class CircularRingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var elements: MutableList<CircularRingElement>? = null

    //内圈小圆的圆心
    private var innerCircleX = 0f
    private var innerCircleY = 0f
    //内圈小圆的半径
    private var innerCircleRadius = 0f

    //View的宽高
    private var mWidth = 0
    private var mHeight = 0

    //圆环宽度
    private var mStrokeWidth = dp2px(10f)

    //画笔
    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.FILL
            strokeWidth = 10f
        }
    }
    //扇形所在的矩形区域
    private val rectF = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)

        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mWidth = dp2px(200f) //设置默认宽高
        }
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mHeight = dp2px(200f)
        }

        //外圈扇形所在矩形区域
        rectF.run {
            left = 0f + paddingLeft
            top = 0f + paddingTop
            right = mWidth.toFloat() - paddingRight
            bottom = mHeight.toFloat() - paddingBottom
        }

        innerCircleX = mWidth * 0.5f
        innerCircleY = mHeight * 0.5f

        innerCircleRadius = mWidth * 0.5f - mStrokeWidth - (paddingStart + paddingEnd)

        //保存测量结果
        setMeasuredDimension(mWidth, mHeight)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var startAngle = 0f
        var valueAll = 0f
        elements?.run {
            all {
                valueAll += it.value
                true
            }
            for (entry in this) {
                val sweepAngle = entry.value / valueAll * 360f
                canvas?.drawArc(rectF, startAngle, sweepAngle, true, mPaint.apply { color = entry.color })
                startAngle += sweepAngle
            }
        }
        canvas?.drawCircle(innerCircleX, innerCircleY, innerCircleRadius, mPaint.apply { color = Color.WHITE })
    }

    /**
     * 设置数据元素
     */
    fun setElementList(elements: MutableList<CircularRingElement>) {
        this.elements = elements
        postInvalidate()
    }

    /**
     * 设置圆环厚度
     */
    fun setRingThickness(value: Int) {
        this.mStrokeWidth = value
    }

    /**
     * 数据元素
     */
    data class CircularRingElement(@ColorInt val color: Int, val value: Float)


}