# statisticsdemo
简单的自定义View，圆环统计

## 效果图
最近项目中需要用到一个圆环统计，如下图所示，于是手撸了一个超级简单的分享给大家。
就当做给萌新自定义View的入门了，有需要的也可以直接拷贝过去就能用，先看效果图

![statisticsImg](https://github.com/qingtian521/PhotoResources/blob/master/image_circle_ring.png)

## 日常分析一波
先构思一波，这个东西应该怎么实现，因为本身没有能直接画出圆环的api，所以我们需要换一个角度来，
可以用扇形来表示圆，中间加个小圆形覆盖在上面，这样就成了我们看到的圆环了。有了思路再一看这个，就很简单了。
剩下的就是控制几段，所占比例，以及颜色了。
所以只需要用到 drawCircle 和 drawArc 画圆和扇形
~~~
public void drawArc(@RecentlyNonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter, @RecentlyNonNull Paint paint) {
     throw new RuntimeException("Stub!");
}

public void drawCircle(float cx, float cy, float radius, @RecentlyNonNull Paint paint) {
     throw new RuntimeException("Stub!");
}

~~~

## 撸代码
### 1.先准备数据元素
首先咱们要确定有几段数据，每段的颜色用什么标识，同时所占比例是多少。这些都不确定，需要可以手动设置，所以把他们放在一个实体类里面
~~~
    fun setElementList(elements: MutableList<CircularRingElement>) {
        this.elements = elements
        postInvalidate()
    }
    
    data class CircularRingElement(@ColorInt val color: Int, val value: Float)
~~~
### 2.计算View的大小
同时将圆心坐标，半径，以及扇形所在区域的矩形一并计算出来了
~~~
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
~~~
### 3.Draw
最后再根据数据画出对应的扇形和内圈圆就OK了
~~~ 
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
~~~
## 全部代码
方便需要的朋友直接拷贝了
~~~
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
     * 设置圆环厚度
     */
    fun setRingThickness(value: Int) {
        this.mStrokeWidth = value
    }

    /**
     * 设置数据元素
     */
    fun setElementList(elements: MutableList<CircularRingElement>) {
        this.elements = elements
        postInvalidate()
    }

    /**
     * 数据元素
     */
    data class CircularRingElement(@ColorInt val color: Int, val value: Float)
}
~~~
总体来说，很简单，本来想着不传上来的，但是想着以后可能又会遇到其他各种各样的统计图，
于是乎专门新建了这个工程，到时候可以直接归个总，避免重复造轮子了。同时有朋友要是有别的需求可以留言出来，
我抽空可以帮忙研究研究，就当作提高自己了
