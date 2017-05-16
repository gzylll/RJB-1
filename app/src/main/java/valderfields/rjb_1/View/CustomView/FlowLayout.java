package valderfields.rjb_1.View.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11650 on 2017/5/15.
 */

public class FlowLayout extends ViewGroup {
    //存储所有子View
    private List<List<View>> mChildViews = new ArrayList<>();
    //每一行的高度
    private List<Integer> mHeight = new ArrayList<>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 根据子控件调整自己的宽度和高度
     * @param widthMeasureSpec 父控件传入的宽度及测量模式
     * @param heightMeasureSpec 父控件传入的高度及测量模式
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度和高度以及对应的测量模式
        int sWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sHeight = MeasureSpec.getSize(heightMeasureSpec);
        int mHeight = MeasureSpec.getMode(heightMeasureSpec);

        //中间变量，初始化为0，即控件中没有子View
        int width = 0;
        int height = 0;
        //每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        //根据子view设置宽度和高度
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            Log.e("OnMeasure","width:"+width+"height:"+height);
            //获取子组件并测量宽和高
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到本组件的LayoutParams，计算出实际的子View的宽和高
            //计算公式为子组件的宽和高加上对应的内边距：margin
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //当本行不足以显示该组件时将其挪至下一行
            if (lineWidth + childWidth > sWidth) {
                //设置组件宽度为组件宽度和行宽中的最大值
                width = Math.max(width, lineWidth);
                //重置新一行宽度lineWidth为组件宽度
                lineWidth = childWidth;
                //记录行高
                height += lineHeight;
                //刷新下一行行高
                lineHeight = childHeight;
            } else {
                //叠加行宽
                lineWidth += childWidth;
                //得到最大行高
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //对于最后一个特殊处理，刷新总的行高与宽度。
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        //MeasureSpec.EXACTLY时组件宽度和高度已制定，否则用测量值。
        setMeasuredDimension(mWidth == MeasureSpec.EXACTLY ? sWidth : width,
                mHeight == MeasureSpec.EXACTLY ? sHeight : height);
    }

    /**
     * 排列子组件的位置，一行可以显示就显示，否则下移一行。
     * @param changed 表示view是否有新的尺寸或位置
     * @param l Left
     * @param t Top
     * @param r Right
     * @param b Bottom
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //清空存储
        mChildViews.clear();
        mHeight.clear();
        //当前宽度，因为我们只是一行放不下考虑另一行，只要宽度属性即可
        int width = getWidth();
        //当前行
        int lineWidth = 0;
        int lineHeight = 0;
        //当前行的view列表
        List<View> lineViews = new ArrayList<View>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            //获取子View以及组件宽高
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //如果显示不下就重置一行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {
                //记录LineHeight
                mHeight.add(lineHeight);
                //记录当前行的Views
                mChildViews.add(lineViews);
                //重置行的宽高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                //重置view的集合
                lineViews = new ArrayList();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        //处理最后一行
        mHeight.add(lineHeight);
        mChildViews.add(lineViews);

        //设置子View的位置
        int left = 0;
        int top = 0;
        //获取行数
        int lineCount = mChildViews.size();
        for (int i = 0; i < lineCount; i++) {
            //当前行的views和高度
            lineViews = mChildViews.get(i);
            lineHeight = mHeight.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                //判断是否显示
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                //进行子View进行布局
                child.layout(cLeft, cTop, cRight, cBottom);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
