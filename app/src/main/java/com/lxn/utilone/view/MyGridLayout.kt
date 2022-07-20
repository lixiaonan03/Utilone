package com.lxn.utilone.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridView
import com.lxn.utilone.R
import kotlin.math.roundToInt

/**
 * @author wangaihu
 * @date 2021/7/7
 * @desc 这个类是一个简单的表格布局，为赠品说明页 [com.wanwu.wanwu.module.mall.present.PresentDetailActivity] 提供表格支持
 * android 自带的两个表格布局，[GridLayout] 和 [GridView] 都有些问题，具体表现为:
 * [GridLayout]: 表现为列宽无法固定。如果第一行使用了 "1 : 1.75 : 1" 的列宽比例，第二行即使指定了相同的比例也无法固定；
 * [GridView]: 表现为每个单元格的宽高只能是相同的，无法做到自适应
 *
 * 在固定好列宽的比例后，[GiftDetailGridLayout] 将每行中最高的 item 高度作为这一行的高度。布局时会根据 [GiftDetailGridLayout.LayoutParams.gravity] 属性
 * 动态摆放每个 item。同样地，如果固定了行高比例，[GiftDetailGridLayout] 就会将每列中最宽的 item 宽度作为这一列的宽度。
 *
 * [GiftDetailGridLayout] 没有复用机制(下文的 sticky 属性勉强算是)，因此会一次性地将所有 view item 展示出来。如果表格数据较多，请注意性能问题。
 *
 * 您可以使用 [R.styleable.GiftDetailGridLayout_columnWeight] 和 [R.styleable.GiftDetailGridLayout_android_columnCount] 指定每列的宽度比例。
 * 对于前者，这个属性对应一个字符串，以英文的 ":" 为分割符，分割后的每个 float 即表示列宽比例。例如 "1:1.75:1"，即表示表格一共有三列，列宽比例为 1 : 1.75 : 1；
 * 对于后者，该属性是整数类型，意为："将宽度平均分割为 columnCount 份"。例如 `android:columnLayout=3` 和 `app:columnWeight="1:1:1"` 是等价的。
 * 请注意：这两个属性不能一起用，即最多只能用二者中的其中一个，否则会抛出 [UnsupportedOperationException] 异常。
 * 除了 xml 属性，您也可以调用 [setColumnWeightArray] 和 [setColumnCount] 这两个 java API，它们会起到同样的效果
 *
 * [R.styleable.GiftDetailGridLayout_rowWeight] [R.styleable.GiftDetailGridLayout_android_rowCount]
 * [setRowWeightArray]  [setRowCount] 的用法和上述类似，不再赘述
 *
 * 另外需要注意的是，在 [onMeasure] 过程中，会对父布局传过来的 [View.MeasureSpec] 约束进行检查。不合适的参数或配置可能会导致您达不到预期的效果。
 * 1. 在宽度已经确定，高度不确定时，您必须显式指定每列的宽度，必须不指定每行的高度。试想在这种情况下，如果指定了每行的高度，我们无法将不确定的高度按比例
 * 分配到每一行上；如果未指定每列的宽度，我们也没有办法将一个确定的宽度分配给不确定的列宽
 *
 * 2. 在宽度不确定，高度已经确定时，您必须显式指定每行的高度，必须不指定每列的宽度;
 *
 * 3. 如果宽/高都已经确定，只要列宽/行高至少有一个已经显式指定了就好
 *
 * [R.styleable.GiftDetailGridLayout_Layout_android_layout_gravity] 指定了 view item 在单元格内的摆放方式。
 * 比如某个 view item 的 measureHeight 是 10，但这一行的高度为 50，此时这个属性就会起作用。例如：如果设置为
 * [Gravity.CENTER]，view item 会居中摆放
 * [Gravity.FILL]，则会将整个 view item 拉伸，以完全匹配到单元格
 *
 * [R.styleable.GiftDetailGridLayout_Layout_layout_sticky] 是一个 boolean 属性，表示这个 view item 是否是 "粘性的"。
 * 它只能在 xml 中设置，通常用于表格的 "表头"。当这个值为 true 时，即使调用 [removeAllViews]，这个 view item 也不会被移除。
 * 当然了，您可以使用 [removeAllViews] 的重载函数，传入参数 `removeSticky` 使用 true，即可完全移除所有 view item
 * （注：这个属性有点不稳定）
 */
class MyGridLayout @JvmOverloads constructor(context: Context,attributeSet:AttributeSet? = null):ViewGroup(context,attributeSet) {

    companion object{
        private fun toWeightArray(string: String?):FloatArray{
            string ?: return FloatArray(0)

            val strings = string.split(":")
            val floats = FloatArray(strings.size)

            strings.forEachIndexed { index, s ->
                try {
                    floats[index] = s.toFloat()
                }
                catch (e: NumberFormatException) {
                    throw NumberFormatException("only float format allowed $e")
                }
            }
            return floats
        }
    }
    /**
     * 子View是否完成大小计算
     */
    private var hasChildrenMeasured:Boolean = false

    private val cachedRowHeight = ArrayList<Int>()
    private val cachedColumnWidth = ArrayList<Int>()

    private var columnWeight = FloatArray(0)
    private var rowWeight = FloatArray(0)

    var rowCount:Int
         get() = rowWeight.size
         set(value) {
             rowWeight = FloatArray(value){1F}
             requestLayout()
         }

    var columnCount: Int
        get() = columnWeight.size
        set(value) {
            columnWeight = FloatArray(value) { 1F }
            requestLayout()
        }
    init {
        setWillNotDraw(false)
        val typedArray = context.obtainStyledAttributes(attributeSet,R.styleable.GiftDetailGridLayout)
        try {
            // 初始化 rowCount 和 rowWeight
            val rowCount = typedArray.getInt(R.styleable.GiftDetailGridLayout_android_rowCount, -1)
            val rowWeight = typedArray.getString(R.styleable.GiftDetailGridLayout_rowWeight)

            if (rowWeight != null && rowCount != -1) {
                throw UnsupportedOperationException(
                    "you're using both row weight array and row count, it's confusing\n" +
                            "consider remove one of them."
                )
            }
            else if (rowCount != -1) {
                this.rowCount = rowCount
            }
            else if (rowWeight != null) {
                setRowWeightArray(toWeightArray(rowWeight))
            }

            // 初始化 columnCount 和 columnWeight
            val columnCount = typedArray.getInt(R.styleable.GiftDetailGridLayout_android_columnCount, -1)
            val columnWeight = typedArray.getString(R.styleable.GiftDetailGridLayout_columnWeight)

            if (columnWeight != null && columnCount != -1) {
                throw UnsupportedOperationException(
                    "you're using both column weight array and column count, it's confusing\n" +
                            "consider remove one of them."
                )
            }
            else if (columnCount != -1) {
                this.columnCount = columnCount
            }
            else if (columnWeight != null) {
                setColumnWeightArray(toWeightArray(columnWeight))
            }

        }
        finally {
            typedArray.recycle()
        }
    }
    fun setRowWeightArray(row: FloatArray): MyGridLayout {
        rowWeight = row.clone()
        requestLayout()
        return this
    }

    fun setColumnWeightArray(column: FloatArray): MyGridLayout {
        columnWeight = column.clone()
        requestLayout()
        return this
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //如果子view 没有完成测量就啥也不做
        if(! hasChildrenMeasured){
            return
        }
        if(columnWeight.isNotEmpty()){
            //一行一行的画
            layoutByRowAndRow(paddingLeft,paddingTop)
        }else{
            layoutByColumnAndColumn(paddingLeft,paddingRight)
        }

    }

    private fun layoutByRowAndRow(l:Int,t:Int){
         var left = l
         var top  = t
         val childCount = this.childCount
         var rowIndex =0
         var columnIndex =0
         for(i in 0 until childCount){
             val child = getChildAt(i)
             if(child.visibility == View.GONE){
                 continue
             }
             val childWidth = cachedColumnWidth[columnIndex]
             val childHeight = cachedRowHeight[rowIndex]
             layoutChildWithGravity(child,left,top,left+childWidth,top+childHeight)
             left += childWidth
             if(++columnIndex == columnWeight.size){
                 columnIndex =0
                 rowIndex++
                 left =l
                 top+=childHeight
             }

         }

    }
    private fun layoutByColumnAndColumn(l: Int, t: Int) {
        var left = l
        var top = t
        var rowIndex = 0; var columnIndex = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)

            if (child.visibility == GONE) {
                continue
            }
            val childWidth = cachedColumnWidth[columnIndex]
            val childHeight = cachedRowHeight[rowIndex]

            layoutChildWithGravity(child, left, top, left + childWidth, top + childHeight)

            top += childHeight

            if (++rowIndex == rowWeight.size) {
                rowIndex = 0
                columnIndex ++
                top = t
                left += childWidth
            }
        }
    }

    private val tempInRect = Rect()
    private val tempOutRect = Rect()

    /**
     * 自定义布局控制器
     */
    class LayoutParams : ViewGroup.MarginLayoutParams {

        var gravity: Int = Gravity.CENTER

        var sticky: Boolean = false

        @JvmOverloads
        constructor(width: Int = MATCH_PARENT, height: Int = MATCH_PARENT) : super(width, height)

        constructor(params: ViewGroup.LayoutParams) : super(params) {
            if (params is LayoutParams) {
                gravity = params.gravity
            }
        }

        constructor(params: MarginLayoutParams) : super(params) {
            if (params is LayoutParams) {
                gravity = params.gravity
            }
        }

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GiftDetailGridLayout_Layout)
            try {
                sticky = typedArray.getBoolean(R.styleable.GiftDetailGridLayout_Layout_layout_sticky, false)
                gravity = typedArray.getInt(R.styleable.GiftDetailGridLayout_Layout_android_layout_gravity, Gravity.CENTER)
            }
            finally {
                typedArray.recycle()
            }
        }
    }
    private fun layoutChildWithGravity(child: View, l: Int, t: Int, r: Int, b: Int) {
        val lp = child.layoutParams as LayoutParams
        tempInRect.set(l + lp.leftMargin, t + lp.topMargin, r - lp.rightMargin, b - lp.bottomMargin)
        tempOutRect.set(0, 0, 0, 0)

        Gravity.apply(lp.gravity, child.measuredWidth, child.measuredHeight, tempInRect, tempOutRect, layoutDirection)

        child.layout(tempOutRect.left, tempOutRect.top, tempOutRect.right, tempOutRect.bottom)
    }

    override fun removeAllViews() {
       removeAllViews(false)
    }

    private fun removeAllViews(removeSticky:Boolean){
        if(removeSticky){
            super.removeAllViews()
        }else{
            val sticky = ArrayList<View>()
            for (i in 0 until childCount){
                val child = getChildAt(i)
                val lp = child.layoutParams as LayoutParams
                if(lp.sticky){
                    sticky.add(child)
                }
            }
            super.removeAllViews()
            sticky.forEach{ addView(it) }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        hasChildrenMeasured = false
        cachedRowHeight.clear()
        cachedColumnWidth.clear()
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode != MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            // 如果宽度已限制，高度没有限制，此时必须指定列数，必须不指定行数
            // 要不然高度没有限制，又限制了行数，这要怎么布局，根本没办法布局啊
            if (columnWeight.isNotEmpty() && rowWeight.isEmpty()) {
                measureByRowAndRow(widthSize)
            }
        }
        else if (widthMode == MeasureSpec.UNSPECIFIED && heightMode != MeasureSpec.UNSPECIFIED) {
            // 如果宽度没有设限制，高度受限，同理，必须指定列数，必须不指定行数
            if (columnWeight.isNotEmpty() && rowWeight.isEmpty()) {
                measureByColumnAndColumn(heightSize)
            }
        }
        else if (widthMode != MeasureSpec.UNSPECIFIED && heightMode != MeasureSpec.UNSPECIFIED) {
            // 如果宽度和高度同时受限，只要行数和列数至少有一个指定就可以
            if (rowWeight.isNotEmpty() && columnWeight.isNotEmpty()) {
                measureByGrid(widthSize, heightSize)
            }
            else if (rowWeight.isNotEmpty() && columnWeight.isEmpty()) {
                measureByColumnAndColumn(heightSize)
            }
            else if (rowWeight.isEmpty() && columnWeight.isNotEmpty()) {
                measureByRowAndRow(widthSize)
            }
        }
    }

    // 按行遍历，遍历完就下一行，将每行最高的高度作为整行的高度
    private fun measureByRowAndRow(widthSize: Int) {
        // 记录每列的实际宽度
        var columnWeightSum = 0F
        val width = widthSize - paddingLeft - paddingRight
        columnWeight.forEach { columnWeightSum += it }
        columnWeight.forEach { cachedColumnWidth.add((it / columnWeightSum * width).roundToInt()) }


        var rowHeight = 0
        var columnIndex = 0
        var totalHeight = paddingTop + paddingBottom

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams

            if (child.visibility == GONE) {
                continue
            }

            val widthSpec = MeasureSpec.makeMeasureSpec(
                cachedColumnWidth[columnIndex] - lp.leftMargin - lp.rightMargin,
                if ((lp.gravity and Gravity.FILL_HORIZONTAL) != 0) { MeasureSpec.EXACTLY } else { MeasureSpec.AT_MOST }
            )
            val heightSpec = MeasureSpec.makeMeasureSpec(
                0, MeasureSpec.UNSPECIFIED
            )

            measureChild(child, widthSpec, heightSpec)


            // 这一行的高度设置为最高的 child 的高度
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            if (childHeight > rowHeight) {
                rowHeight = childHeight
            }

            // 如果到了每行的最后一个，或者所有 children 的最后一个，累加到总高度
            if (++columnIndex == columnWeight.size || i == childCount - 1) {
                cachedRowHeight.add(rowHeight)
                totalHeight += rowHeight
                columnIndex = 0
                rowHeight = 0
            }
        }

        hasChildrenMeasured = true
        setMeasuredDimension(widthSize, totalHeight)
    }

    // 按列遍历，遍历完就下一列，将每列最宽的宽度作为整列的宽度
    private fun measureByColumnAndColumn(heightSize: Int) {
        // 记录每行的实际高度
        var rowHeightSum = 0F
        val height = heightSize - paddingTop - paddingBottom
        rowWeight.forEach { rowHeightSum += it }
        rowWeight.forEach { cachedRowHeight.add((it / rowHeightSum * height).roundToInt()) }


        var rowIndex = 0
        var columnWidth = 0
        var totalWidth = paddingLeft + paddingRight

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams

            if (child.visibility == GONE) {
                continue
            }

            val widthSpec = MeasureSpec.makeMeasureSpec(
                0, MeasureSpec.UNSPECIFIED
            )
            val heightSpec = MeasureSpec.makeMeasureSpec(
                cachedRowHeight[rowIndex] - lp.topMargin - lp.bottomMargin,
                if ((lp.gravity and Gravity.FILL_VERTICAL) != 0) { MeasureSpec.EXACTLY } else { MeasureSpec.AT_MOST }
            )

            measureChild(child, widthSpec, heightSpec)


            // 这一列的宽度设置为最宽的 child 的宽度
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            if (childWidth > columnWidth) {
                columnWidth = childWidth
            }

            // 如果到了每列的最后一个，或者所有 children 的最后一个，累加到总宽度
            if (++rowIndex == rowWeight.size || i == childCount - 1) {
                cachedColumnWidth.add(columnWidth)
                totalWidth += columnWidth
                columnWidth = 0
                rowIndex = 0
            }
        }

        hasChildrenMeasured = true
        setMeasuredDimension(totalWidth, heightSize)
    }


    // 此时行数和列数都已经指定，我们可以确定每个子 view 的位置了
    private fun measureByGrid(widthSize: Int, heightSize: Int) {paddingRight
        var columnWeightSum = 0F
        val width = widthSize - paddingLeft - paddingRight
        columnWeight.forEach { columnWeightSum += it }
        columnWeight.forEach { cachedColumnWidth.add((it / columnWeightSum * width).roundToInt()) }

        var rowHeightSum = 0F
        val height = heightSize - paddingTop - paddingBottom
        rowWeight.forEach { rowHeightSum += it }
        rowWeight.forEach { cachedRowHeight.add((it / rowHeightSum * height).roundToInt()) }


        var rowIndex = 0; var columnIndex = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams

            if (child.visibility == GONE) {
                continue
            }

            val widthSpec = MeasureSpec.makeMeasureSpec(
                cachedColumnWidth[columnIndex] - lp.leftMargin - lp.rightMargin,
                if ((lp.gravity and Gravity.FILL_HORIZONTAL) != 0) { MeasureSpec.EXACTLY } else { MeasureSpec.AT_MOST }
            )
            val heightSpec = MeasureSpec.makeMeasureSpec(
                cachedRowHeight[rowIndex] - lp.topMargin - lp.bottomMargin,
                if ((lp.gravity and Gravity.FILL_VERTICAL) != 0) { MeasureSpec.EXACTLY } else { MeasureSpec.AT_MOST }
            )

            measureChild(child, widthSpec, heightSpec)

            if (++columnIndex == columnWeight.size) {
                // 已经到了这一行的最后一列，准备换行
                columnIndex = 0
                rowIndex ++
            }
        }

        hasChildrenMeasured = true
        setMeasuredDimension(widthSize, heightSize)
    }


    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        if (p is MarginLayoutParams) {
            return LayoutParams(p)
        }
        return LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams()
    }

    interface Decoration {
        fun onPreDraw(view: MyGridLayout, canvas: Canvas) {  }
        fun onPostDraw(view: MyGridLayout, canvas: Canvas) {  }

        fun onPreDispatchDraw(view: MyGridLayout, canvas: Canvas) {  }
        fun onPostDispatchDraw(view: MyGridLayout, canvas: Canvas) {  }

        fun onPreOnDraw(view: MyGridLayout, canvas: Canvas) {  }
        fun onPostOnDraw(view: MyGridLayout, canvas: Canvas) {  }
    }

    var decoration: Decoration? = null

    override fun dispatchDraw(canvas: Canvas) {
        decoration?.onPreDispatchDraw(this, canvas)
        super.dispatchDraw(canvas)
        decoration?.onPostDispatchDraw(this, canvas)
    }

    override fun draw(canvas: Canvas) {
        decoration?.onPreDraw(this, canvas)
        super.draw(canvas)
        decoration?.onPostDraw(this, canvas)
    }

    override fun onDraw(canvas: Canvas) {
        decoration?.onPreOnDraw(this, canvas)
        super.onDraw(canvas)
        decoration?.onPostOnDraw(this, canvas)
    }

    fun getRowHeight(index: Int) =
        if (index in 0 until cachedRowHeight.size) cachedRowHeight[index] else 0

    fun getColumnWidth(index: Int) =
        if (index in 0 until cachedColumnWidth.size) cachedColumnWidth[index] else 0
}