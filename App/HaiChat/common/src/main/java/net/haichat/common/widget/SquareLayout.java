package net.haichat.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SquareLayout extends FrameLayout {
    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 覆写 测量方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 基于宽度的正方形
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
