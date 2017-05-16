package com.hgd.circlememudemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：huanggd on 2017/5/11 15:12
 */
public class CircleMemuLayout extends ViewGroup {

    //circle radius
    private int mRadius;
    /**
     * The constant DEFULT_CHILD_DIMENSION.
     */
    public static final float DEFULT_CHILD_DIMENSION = 1 / 4f;
    /**
     * The constant RADIO_PADDING_LAYOUT.
     */
    /**
     * 菜单的中心child的默认尺寸
     */
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;
    /**
     * The constant RADIO_PADDING_LAYOUT.
     */
    public static final float RADIO_PADDING_LAYOUT = 1 / 16f;
    // 内边距
    private float mPadding;
    //布局开始角度
    private double mStartAngle = 0;

    // 布局资源
    private int mMemuItemLayoutId;

    /**
     * Sets adapter.
     *
     * @param adapter the adapter
     */
    public void setAdapter(CircleMenuAdapter adapter) {
        this.adapter = adapter;
    }

    private CircleMenuAdapter adapter;
    private ImageView mCenterView;
    private int centerId;

    @Override
    protected void onAttachedToWindow() {
        if (adapter != null) buildMemuItem();
        else {
            super.onAttachedToWindow();
        }
    }

    private void buildMemuItem() {
        for (int i = 0; i < adapter.getCount(); i++) {
            final View item = adapter.getView(i, null, this);
            addView(item);
        }
        mCenterView = adapter.getImageView();
        centerId = mCenterView.getId();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mCenterView, layoutParams);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureMyself(widthMeasureSpec, heightMeasureSpec);
        measureChildViews();
    }


    private void measureMyself(int widthMeasureSpec, int heightMeasureSpec) {
        int resWith = 0;
        int resHeight = 0;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //宽高不是精确值
        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            resWith = getSuggestedMinimumWidth();
            resHeight = getSuggestedMinimumHeight();
            resWith = resWith == 0 ? getDefaultWidth() : resWith;
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {//直接取最小值
            resWith = resHeight = Math.min(width, height);
        }
        setMeasuredDimension(resWith, resHeight);
    }


    private void measureChildViews() {
        //获得半径
        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());
        //获取item count
        int count = getChildCount();
        //item size
        int childSize = (int) (mRadius * DEFULT_CHILD_DIMENSION);
        int childMode = MeasureSpec.EXACTLY;
        //迭代测量
        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i);
            if (view.getVisibility() == GONE) continue;
            int makeMeasureSpec = -1;

            if (view.getId() == centerId) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION), childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            }
            view.measure(makeMeasureSpec, makeMeasureSpec);

        }
        mPadding = mRadius * RADIO_PADDING_LAYOUT;
    }

    /**
     * Sets memu item layout id.
     *
     * @param mMemuItemLayoutId the m memu item layout id
     */
    public void setmMemuItemLayoutId(int mMemuItemLayoutId) {
        this.mMemuItemLayoutId = mMemuItemLayoutId;
    }


    /**
     * Instantiates a new Circle memu layout.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CircleMemuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //无视padding
        setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("hgd", "onLayout" + "changed" + changed + "l" + l + "t" + t + "r" + r + "b" + b);
        final int childCount = getChildCount();
        int left, top;
        //menu item 的尺寸
        int itemWith = (int) (mRadius * DEFULT_CHILD_DIMENSION);
        //caculate angle
        float angleDely = 360 / (childCount - 1);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            if (child.getId() == centerId) {

                int cl = mRadius / 2 - child.getMeasuredWidth() / 2;
                int cr = cl + child.getMeasuredWidth();
                child.layout(cl, cl, cr, cr);
            } else {
                mStartAngle %= 360;
                float distenceFromCenter = mRadius / 2f - itemWith / 2 - mPadding;
                left = mRadius / 2 - itemWith / 2 + (int) (Math.round(distenceFromCenter) * Math.cos(Math.toRadians(mStartAngle
                )));
                top = mRadius / 2 - itemWith / 2 + (int) (Math.round(distenceFromCenter) * Math.sin(Math.toRadians(mStartAngle
                )));
                child.layout(left, top, left + itemWith, top + itemWith);
                mStartAngle += angleDely;
            }
        }
    }


    /**
     * 获得默认该layout的尺寸
     *
     * @return
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    //记录上一次的坐标
    private float mLastX;

    private float mLastY;
    private boolean isFlying = false;
    //检测按下到抬起转过的角度
    private float mTmpAngle = 0;
    //检测按下到抬起的时间
    private long mDownTime;
    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private static final int FLINGABLE_VALUE = 300;

    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private static final int NOCLICK_VALUE = 3;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private int mFlingableValue = FLINGABLE_VALUE;

    /**
     * 自动滚动的Runnable
     */
    private AutoFlingRunable mFlingRunnable;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        //添加判断在中心区域的话停止，不处理
        int cl = mRadius / 2 - mCenterView.getMeasuredWidth() / 2;
        int cr = cl + mCenterView.getMeasuredWidth();

        Log.i("hgd", "action down  240 480 x :" + x + "y: " + y);
        if ((x >= cl && x <= cr && y >= cl && y <= cr) || x > mRadius && y > mRadius) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mTmpAngle = 0;
                mDownTime = System.currentTimeMillis();
                if (isFlying) {
                    removeCallbacks(mFlingRunnable);
                    isFlying = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float start = getAngle(mLastX, mLastY);
                float end = getAngle(event.getX(), event.getY());
//                mStartAngle += Math.abs(end - start);
//                mTmpAngle += Math.abs(end - start);
                Log.e("hgd", "start = " + start + " , end =" + end);
//                 如果是一、四象限，则直接end-start，角度值都是正值

                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += end - start;
                    mTmpAngle += end - start;
                } else
                // 二、三象限，色角度值是付值
                {
                    mStartAngle += start - end;
                    mTmpAngle += start - end;
                }
                Log.i("hgd", "startAngle" + mStartAngle);
                requestLayout();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                float anglePerSecond = mTmpAngle * 1000 / (System.currentTimeMillis() - mDownTime);
                if (Math.abs(anglePerSecond) > mFlingableValue && !isFlying) {
                    post(mFlingRunnable = new AutoFlingRunable(anglePerSecond));
                    return true;
                }
                if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
                    //禁止点击
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 主要为了action_down时，返回true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }

    /**
     * 根据触摸的位置，计算角度
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    private class AutoFlingRunable implements Runnable {


        private float anglePerSec;

        /**
         * Instantiates a new Auto fling runable.
         *
         * @param anglePerSec the angle per sec
         */
        public AutoFlingRunable(float anglePerSec) {
            this.anglePerSec = anglePerSec;
        }

        @Override
        public void run() {
            if (Math.abs(anglePerSec) < 20) {
                isFlying = false;
                return;
            }
            isFlying = true;
            // 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
            mStartAngle += (anglePerSec / 20);
            // 逐渐减小这个值
            anglePerSec /= 1.0666F;
            postDelayed(this, 30);
            // 重新布局
            requestLayout();
        }

    }

    /**
     * The type Builder.
     */
    public static class Builder {
        private Context context;
        private List<CircleMenuAdapter.MemuItem> mMenuItmlsit;

        /**
         * Gets center view.
         *
         * @return the center view
         */
        public ImageView getCenterView() {
            return imageView;
        }

        private ImageView imageView;

        /**
         * Instantiates a new Builder.
         *
         * @param context the context
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Add menu item builder.
         *
         * @param title the title
         * @param icon  the icon
         * @param act   the act
         * @return the builder
         */
        public Builder addMenuItem(String title, int icon, final Class<? extends Activity> act) {
            if (mMenuItmlsit == null)
                mMenuItmlsit = new ArrayList<CircleMenuAdapter.MemuItem>();
            mMenuItmlsit.add(new CircleMenuAdapter.MemuItem(icon, title, act));

            return this;
        }

        /**
         * Add centertem builder.
         *
         * @param icon the icon
         * @param act  act that will start
         * @return the builder
         */
        public Builder addCentertem(int icon, final Class<?> act) {
            if (imageView == null)
                imageView = new ImageView(context);
            imageView.setId(generateViewId());
            imageView.setImageResource(icon);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (act != null) {
                        Intent i = new Intent(context, act);
                        context.startActivity(i);
                        return;
                    }
                }
            });

            return this;
        }

        /**
         * Gets menu adapter.
         *
         * @return the menu adapter
         */
        public CircleMenuAdapter getcircleMenuAdapter() {
            CircleMenuAdapter circleMenuAdapter = new CircleMenuAdapter(mMenuItmlsit, context, imageView);
            circleMenuAdapter.setOnItemClickListener(new CircleMenuAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Adapter parent, CircleMenuAdapter.MemuItem view, int position, long id) {
                    Class<?> act = view.getActivity();
                    if (act != null) {
                        Intent i = new Intent(context, act);
                        context.startActivity(i);
                        return;
                    }
                }
            });
            return circleMenuAdapter;
        }
    }
}
