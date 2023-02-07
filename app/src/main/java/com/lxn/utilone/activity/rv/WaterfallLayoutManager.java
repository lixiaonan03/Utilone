package com.lxn.utilone.activity.rv;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author lixiaonan
 * 功能描述: 跟艾虎学习的瀑布流的布局文件
 * 时 间： 2023/2/1 11:05
 */
public class WaterfallLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {

    private static final String TAG = "WaterfallLayoutManager";

    public static class LayoutParams extends RecyclerView.LayoutParams {


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

        Span span = null;
    }

    private final int INVALID_INT = Integer.MIN_VALUE;

    private final class Span {
        final int index;

        Span(int index) {
            this.index = index;
        }

        int cachedStart = INVALID_INT;
        int cachedEnd = INVALID_INT;

        private final List<View> children = new ArrayList<>();

        boolean isEmpty() {
            return children.size() == 0;
        }

        @NonNull
        View getFirstView() {
            return children.get(0);
        }

        @NonNull
        View getLastView() {
            return children.get(children.size() - 1);
        }


        int getStartLine() {
            if (cachedStart == INVALID_INT) {
                View child = children.get(0);
                cachedStart = primaryOrientation.getDecoratedStart(child);
            }
            return cachedStart;
        }

        int getEndLine() {
            if (cachedEnd == INVALID_INT) {
                View child = children.get(children.size() - 1);
                cachedEnd = primaryOrientation.getDecoratedEnd(child);
            }
            return cachedEnd;
        }

        void addLast(View child) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.span = this;
            children.add(child);
            if (cachedEnd != INVALID_INT) {
                cachedEnd += primaryOrientation.getDecoratedMeasurement(child);
            }
        }

        void addFirst(View child) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.span = this;
            children.add(0, child);
            if (cachedStart != INVALID_INT) {
                cachedStart -= primaryOrientation.getDecoratedMeasurement(child);
            }
        }

        void removeLast() {
            View child = children.remove(children.size() - 1);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.span = null;

            if (cachedEnd != INVALID_INT) {
                cachedEnd -= primaryOrientation.getDecoratedMeasurement(child);
            }
        }

        void removeFirst() {
            View child = children.remove(0);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.span = null;

            if (cachedStart != INVALID_INT) {
                cachedStart += primaryOrientation.getDecoratedMeasurement(child);
            }
        }

        void offset(int amount) {
            if (cachedStart != INVALID_INT) {
                cachedStart += amount;
            }
            if (cachedEnd != INVALID_INT) {
                cachedEnd += amount;
            }
        }

        void clear() {
            children.clear();
            cachedStart = INVALID_INT;
            cachedEnd = INVALID_INT;
        }
    }

    private static final class AnchorInfo {
        int position = -1;

        void reset() {
            position = -1;
        }
    }

    private static final class LazySpanIndexLookup {
        private byte[] array;

        int get(int position) {
            if (array == null || array.length < position) {
                return -1;
            }
            return array[position];
        }

        private void ensureSize(int size) {
            final int oldSize = (array == null) ? 0 : array.length;
            if (oldSize >= size) {
                return;
            }
            size = Math.max(size, 32);
            size = Math.max(size, oldSize + (oldSize << 1));
            array = (array == null) ? new byte[size] : Arrays.copyOf(array, size);
            Arrays.fill(array, oldSize, size, (byte) -1);
        }

        void put(int position, int index) {
            ensureSize(position + 1);
            array[position] = (byte) index;
        }

        void move(int fromIndex, int toIndex, int count) {
            ensureSize(Math.max(fromIndex, toIndex) + count);
            if (fromIndex > toIndex) {
                // 从后往前拷贝，不用考虑重叠问题
                for (; count != 0; --count) {
                    array[toIndex++] = array[fromIndex++];
                }
            } else {
                // 从前往后拷贝时，可能存在内存重叠问题，我们逆序遍历
                fromIndex += count - 1;
                toIndex += count - 1;
                for (; count != 0; --count) {
                    array[toIndex--] = array[fromIndex--];
                }
            }
        }

        void clear() {
            if (array != null) {
                Arrays.fill(array, (byte) -1);
            }
        }

        /**
         * 清空从 fromIndex 开始(含)的所有数据
         */
        void clear(int fromIndex) {
            if (array != null && array.length > fromIndex) {
                Arrays.fill(array, fromIndex, array.length, (byte) -1);
            }
        }
    }


    /**
     * 竖直方向的layoutmanager的OrientationHelper
     */
    private final OrientationHelper primaryOrientation = OrientationHelper.createVerticalHelper(this);
    private final OrientationHelper secondaryOrientation = OrientationHelper.createHorizontalHelper(this);
    private final LazySpanIndexLookup spanIndexLookup = new LazySpanIndexLookup();
    private final AnchorInfo anchorInfo = new AnchorInfo();

    private Span[] spans;

    public WaterfallLayoutManager(int spanCount) {
        setSpanCount(spanCount);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.i(TAG, "onLayoutChildren: " + state.isPreLayout());

        anchorInfo.reset();
        updateAnchorInfoForLayout(anchorInfo, state);

        int defaultStart = primaryOrientation.getStartAfterPadding();
        for (Span span : spans) {
            int start = span.isEmpty() ? defaultStart : span.getStartLine();
            span.clear();
            span.cachedStart = span.cachedEnd = start;
        }
        detachAndScrapAttachedViews(recycler);
        calculateSpanSpace();

        fill(recycler, state, anchorInfo.position, 1);
        fill(recycler, state, anchorInfo.position - 1, -1);

        if (!state.isPreLayout()) {
            fixGap(recycler, state);
        }
    }

    private void fixGap(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // todo 瀑布流布局似乎没发现这个问题
    }

    private void updateAnchorInfoForLayout(AnchorInfo anchorInfo, RecyclerView.State state) {
        if (updateAnchorInfoFromChildren(anchorInfo, state)) {
            return;
        }
        anchorInfo.position = 0;
    }

    private boolean updateAnchorInfoFromChildren(AnchorInfo anchorInfo, RecyclerView.State state) {
        int position = findReferenceChild(state, 1);
        if (position != -1) {
            anchorInfo.position = position;
            return true;
        }
        return false;
    }

    private int findReferenceChild(RecyclerView.State state, int direction) {
        final int itemCount = state.getItemCount();
        int from, to, diff;
        if (direction > 0) {
            from = 0;
            to = getChildCount();
            diff = 1;
        } else {
            from = getChildCount();
            to = -1;
            diff = -1;
        }

        for (int i = from; i != to; i += diff) {
            View child = getChildAt(i);
            int position = getPosition(child);
            if (position >= 0 && position < itemCount) {
                return position;
            }
        }
        return -1;
    }

    private int[] spanSpaces;

    private void calculateSpanSpace() {
        int spanCount = spans.length;
        if (spanSpaces == null || spanSpaces.length < spanCount + 1) {
            spanSpaces = new int[spanCount + 1];
        }
        int totalSpace = secondaryOrientation.getTotalSpace();
        float perSpanSpace = 1F * totalSpace / spanCount;
        spanSpaces[0] = primaryOrientation.getStartAfterPadding();
        for (int i = 1; i < spanSpaces.length; ++i) {
            spanSpaces[i] = spanSpaces[0] + (int) (i * perSpanSpace + 0.5F);
        }
    }

    private Span findSpan(int direction) {
        Span span = spans[0];

        if (direction > 0) {
            int end = span.getEndLine();
            for (Span sp : spans) {
                int tmp = sp.getEndLine();
                if (tmp < end) {
                    span = sp;
                    end = tmp;
                }
            }
        } else {
            int start = span.getStartLine();
            for (Span sp : spans) {
                int tmp = sp.getStartLine();
                if (tmp > start) {
                    span = sp;
                    start = tmp;
                }
            }
        }
        return span;
    }


    private int extraLayoutStart = 0, extraLayoutEnd = 0;

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state, int position, int direction) {
        final int end = primaryOrientation.getEndAfterPadding() + extraLayoutEnd;
        final int start = primaryOrientation.getStartAfterPadding() - extraLayoutStart;
        final int totalSpanSpace = secondaryOrientation.getTotalSpace();

        int availableSpanCount = 0;

        if (direction > 0) {
            for (Span span : spans) {
                if (span.isEmpty() || span.getEndLine() < end) {
                    availableSpanCount += 1;
                }
            }
        } else {
            for (Span span : spans) {
                if (span.isEmpty() || span.getStartLine() > start) {
                    availableSpanCount += 1;
                }
            }
        }

        for (int i = position, n = state.getItemCount(); i >= 0 && i < n; i += direction) {
            if (availableSpanCount <= 0) {
                break;
            }

            View child = recycler.getViewForPosition(i);

            // 先准备好 span. 有些 itemDecoration 依赖于 spanIndex
            final Span span;
            int spanIndex = spanIndexLookup.get(i);
            if (spanIndex == -1) {
                span = findSpan(direction);
                spanIndex = span.index;
                spanIndexLookup.put(i, span.index);
            } else {
                span = spans[spanIndex];
            }

            // todo 如果要支持水平方向需要调整此处

            // 开始测量
            int spanSpace = spanSpaces[spanIndex + 1] - spanSpaces[spanIndex];
            measureChildWithMargins(child, totalSpanSpace - spanSpace, 0);

            // 布局
            int l = spanSpaces[spanIndex];
            int r = l + primaryOrientation.getDecoratedMeasurementInOther(child);
            int t, b;
            if (direction > 0) {
                t = span.getEndLine();
                b = t + primaryOrientation.getDecoratedMeasurement(child);
                layoutDecoratedWithMargins(child, l, t, r, b);
                addView(child, -1);
                span.addLast(child);

                if (b >= end) {
                    availableSpanCount -= 1;
                }
            } else {
                b = span.getStartLine();
                t = b - primaryOrientation.getDecoratedMeasurement(child);
                layoutDecoratedWithMargins(child, l, t, r, b);
                addView(child, 0);
                span.addFirst(child);

                if (t <= start) {
                    availableSpanCount -= 1;
                }
            }
        }
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        primaryOrientation.onLayoutComplete();
        secondaryOrientation.onLayoutComplete();
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    private int getSpansEnd() {
        int end = Integer.MIN_VALUE;
        for (Span span : spans) {
            if (!span.isEmpty()) {
                end = Math.max(end, span.getEndLine());
            }
        }
        return end;
    }

    private int getSpansStart() {
        int start = Integer.MAX_VALUE;
        for (Span span : spans) {
            if (!span.isEmpty()) {
                start = Math.min(start, span.getStartLine());
            }
        }
        return start;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 手指从下往上滑动时，dy > 0
        // 手指从上往下滑动时，dy < 0

        final int childCount = getChildCount();
        if (childCount <= 0) {
            return 0;
        }

        int consumed = testScroll(dy, recycler, state);
        extraLayoutStart = extraLayoutEnd = 0;

        if (consumed != 0) {
            primaryOrientation.offsetChildren(-consumed);
            for (Span span : spans) {
                span.offset(-consumed);
            }
            clamp(recycler);
        }
        return consumed;
    }

    private int testScroll(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        final int childCount = getChildCount();
        if (childCount <= 0) {
            return 0;
        }

        if (dy > 0) {
            // 先检查 span 有没有到达屏幕下边界。如果没有，则说明布局没有充满整个屏幕，
            // 我们不会消费此次滑动事件
            int spanEnd = getSpansEnd();
            int parentEnd = primaryOrientation.getEndAfterPadding();

            if (spanEnd < parentEnd) {
                return 0;
            }

            // 拓展下边界, 填充
            extraLayoutEnd = dy;
            View lastChild = getChildAt(childCount - 1);
            fill(recycler, state, getPosition(lastChild) + 1, 1);

            spanEnd = getSpansEnd();
            return dy - Math.max(parentEnd + extraLayoutEnd - spanEnd, 0);
        } else if (dy < 0) {
            // 同理，先检查 span 上边界有没有到达屏幕上边界。没有的话则不消费
            int spanStart = getSpansStart();
            int parentStart = primaryOrientation.getStartAfterPadding();
            if (spanStart > parentStart) {
                return 0;
            }

            // 拓展上边界，填充
            extraLayoutStart = -dy;
            View firstChild = getChildAt(0);
            fill(recycler, state, getPosition(firstChild) - 1, -1);

            spanStart = getSpansStart();
            return dy - Math.min(parentStart - extraLayoutStart - spanStart, 0);
        }
        return 0;
    }

    private void clamp(RecyclerView.Recycler recycler) {
        final int parentStart = primaryOrientation.getStartAfterPadding() - extraLayoutStart;
        final int parentEnd = primaryOrientation.getEndAfterPadding() + extraLayoutEnd;

        for (Span span : spans) {
            while (!span.isEmpty()) {
                View child = span.getFirstView();
                int childEnd = primaryOrientation.getDecoratedEnd(child);
                if (childEnd >= parentStart) {
                    break;
                }
                span.removeFirst();
                removeAndRecycleView(child, recycler);
            }
            while (!span.isEmpty()) {
                View child = span.getLastView();
                int childStart = primaryOrientation.getDecoratedStart(child);
                if (childStart <= parentEnd) {
                    break;
                }
                span.removeLast();
                removeAndRecycleView(child, recycler);
            }
        }
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            public int getVerticalSnapPreference() {
                return SNAP_TO_START;
            }
        };
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    @Nullable
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        if (getChildCount() == 0) {
            return null;
        }
        final int firstChildPos = getPosition(getChildAt(0));
        return new PointF(0F, targetPosition < firstChildPos ? -1F : 1F);
    }

    @Override
    public View findViewByPosition(int position) {
        final int n = getChildCount();
        if (n <= 0) {
            return null;
        }
        final int firstChild = getPosition(getChildAt(0));
        final int viewPosition = position - firstChild;

        if (viewPosition >= 0 && viewPosition < n) {
            final View child = getChildAt(viewPosition);
            if (getPosition(child) == position) {
                return child;   // in pre-layout, this may not match
            }
        }
        // fallback to traversal. This might be necessary in pre-layout.
        return super.findViewByPosition(position);
    }

    @Override
    public void onAdapterChanged(@Nullable RecyclerView.Adapter oldAdapter, @Nullable RecyclerView.Adapter newAdapter) {
        spanIndexLookup.clear();
        for (Span span : spans) {
            span.clear();
        }
    }

    @Override
    public void onItemsChanged(@NonNull RecyclerView recyclerView) {
        spanIndexLookup.clear();
        requestLayout();
    }

    @Override
    public void onItemsAdded(@NonNull RecyclerView recyclerView, int positionStart, int itemCount) {
        // 插入 item 以后，这个位置及以后的 spanIndex 将无效
        spanIndexLookup.clear(positionStart);
        requestLayout();
    }

    @Override
    public void onItemsRemoved(@NonNull RecyclerView recyclerView, int positionStart, int itemCount) {
        spanIndexLookup.clear(positionStart);
        requestLayout();
    }

    @Override
    public void onItemsUpdated(@NonNull RecyclerView recyclerView, int positionStart, int itemCount) {
        spanIndexLookup.clear(positionStart);
        requestLayout();
    }

    @Override
    public void onItemsMoved(@NonNull RecyclerView recyclerView, int from, int to, int itemCount) {
        spanIndexLookup.clear(Math.min(from, to));
        requestLayout();
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            checkForGaps();
        }
    }

    private boolean fixGapWhenIdle = true;

    private void checkForGaps() {
        int childCount = getChildCount();
        if (childCount <= 0 || !fixGapWhenIdle) {
            return;
        }
        // 如果第 0 个 item 没有露出来，不需要处理
        View firstChild = getChildAt(0);
        if (getPosition(firstChild) != 0) {
            return;
        }
        // 没有全部露出来也不行
        int childStart = primaryOrientation.getDecoratedStart(firstChild);
        int parentStart = primaryOrientation.getStartAfterPadding();

        if (childStart < parentStart) {
            return;
        }

        // 正常情况下，每个 span 第 0 个的上边缘应该是对齐的
        boolean fix = false;
        for (Span span : spans) {
            if (!span.isEmpty() && span.getStartLine() != parentStart) {
                fix = true;
                break;
            }
        }
        // 嘿嘿
        if (fix) {
            for (Span span : spans) {
                span.clear();
            }
            spanIndexLookup.clear();
            requestSimpleAnimationsInNextLayout();
            requestLayout();
        }
    }

    public boolean fixGapWhenIdle() {
        return fixGapWhenIdle;
    }

    public void fixGapWhenIdle(boolean enabled) {
        this.fixGapWhenIdle = enabled;
    }

    public void setSpanCount(int spanCount) {
        assertNotInLayoutOrScroll("setSpanCount()");
        if (spanCount <= 0) {
            throw new IllegalArgumentException("spanCount = " + spanCount);
        }
        if (spans == null || spans.length != spanCount) {
            spans = new Span[spanCount];
            for (int i = 0; i < spanCount; ++i) {
                spans[i] = new Span(i);
            }
            requestLayout();
        }
    }


    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof RecyclerView.LayoutParams) {
            return new LayoutParams((RecyclerView.LayoutParams) lp);
        }
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        }
        return new LayoutParams(lp);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
