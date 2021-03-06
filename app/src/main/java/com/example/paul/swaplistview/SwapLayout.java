package com.example.paul.swaplistview;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * Created by Paul on 29/3/17.
 */

public class SwapLayout extends FrameLayout {
    private View contentView;
    private SwapMenuView rightSwapMenuView, leftSwapMenuView;
    private GestureDetector.OnGestureListener gestureListener;
    private GestureDetectorCompat gestureDetector;
    private float beforeX, beforeY, currentX, currentY;
    private int swapStatue;
    private int position;
    private int curSwapPosition = -1;
    private int swapDirection = 0;

    // swap statues indicate the current swapped status {left, origin, right}
    public final static int SWAP_STATUS_LEFT = 1000;
    public final static int SWAP_STATUS_RIGHT = 1001;
    public final static int SWAP_STATUS_ORIGIN = 1002;

    // swap direction indicate the current swap direction {left, right}
    public final static int SWAP_DIRECTION_LEFT = -5;
    public final static int SWAP_DIRECTION_RIGHT = 5;
    public int swapMoveDistance = 150;


    public SwapLayout(View contentView, SwapMenuView leftSwapMenuView, SwapMenuView rightSwapMenuView){
        this(contentView, leftSwapMenuView,rightSwapMenuView, null, null);
    }

    public SwapLayout(View contentView, SwapMenuView leftSwapMenuView, SwapMenuView rightSwapMenuView,  Interpolator closeInterpolator, Interpolator openInterpolator){
        super(contentView.getContext());
        this.contentView = contentView;
        this.leftSwapMenuView = leftSwapMenuView;
        this.rightSwapMenuView = rightSwapMenuView;
        init();
        initSwapListener();

    }

    private void init(){
        setLayoutParams(new AbsListView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        contentView.setLayoutParams(new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        leftSwapMenuView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rightSwapMenuView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(leftSwapMenuView);
        addView(rightSwapMenuView);
        addView(contentView);

        swapStatue = SWAP_STATUS_ORIGIN;

    }

    private void initSwapListener(){
        gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null){
                    // TODO: 30/3/17 check why this could be null
                    return false;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

        };
        gestureDetector = new GestureDetectorCompat(getContext(), gestureListener);
    }

    private boolean isAbleToSwap(MotionEvent event){
        if (curSwapPosition == -1){
            return false;
        }

        if (swapDirection == SWAP_DIRECTION_LEFT) {
            if (contentView.getLeft() < -swapMoveDistance) {
                return false;
            }
        }

        if (swapDirection == SWAP_DIRECTION_RIGHT) {
            if (contentView.getRight() > contentView.getWidth() + swapMoveDistance){
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void onSwap(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                recordPosition(event);
                break;
            case MotionEvent.ACTION_MOVE:
                swapDirection = beforeX - event.getX() > 0 ? SWAP_DIRECTION_LEFT : SWAP_DIRECTION_RIGHT;
                if (isAbleToSwap(event)) {
                    scrollMenuLayout(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                curSwapPosition = -1;
                postCheck();
                break;
        }
    }

    private void recordPosition(MotionEvent event){
        beforeX = event.getX();
        beforeY = event.getY();
        curSwapPosition = position;
    }

    private void scrollMenuLayout(MotionEvent event){
        int leftScroll = (int) ( event.getX() - beforeX + contentView.getLeft());
        if (leftScroll < -swapMoveDistance || leftScroll  > swapMoveDistance){
            leftScroll = swapDirection==SWAP_DIRECTION_LEFT ? -swapMoveDistance : swapMoveDistance ;
        }
        contentView.layout( leftScroll , contentView.getTop(),leftScroll + contentView.getWidth(), contentView.getBottom());
    }

    private void postCheck(){
        if (contentView.getLeft() < -swapMoveDistance/2+1){
            leftSwapMax();
        }else if (contentView.getLeft() > -swapMoveDistance/2  && contentView.getLeft() < swapMoveDistance/2 + 1){
            swapBackToOrigin();
        }else if (contentView.getLeft() > swapMoveDistance/2){
            rightSwapMax();
        }
    }

    private void leftSwapMax(){
        // the distance need to be moved, the final distance location
        applyAnimation(contentView.getLeft() + swapMoveDistance, 0);
        contentView.layout(-swapMoveDistance, contentView.getTop(), -swapMoveDistance + contentView.getWidth(), contentView.getBottom());
        swapStatue = SWAP_STATUS_LEFT;
    }

    private void swapBackToOrigin(){
        applyAnimation(contentView.getLeft(), 0);
        contentView.layout(0, 0, contentView.getWidth(), contentView.getHeight());
        swapStatue = SWAP_STATUS_ORIGIN;
    }


    private void rightSwapMax(){
        applyAnimation(contentView.getLeft() - swapMoveDistance, 0);
        contentView.layout(swapMoveDistance, contentView.getTop(), contentView.getWidth() + swapMoveDistance, contentView.getBottom());
        swapStatue = SWAP_STATUS_RIGHT;
    }


    public void setPosition(int position) {
        this.position = position;
    }

    public void resetToOriginal(){
        if (swapStatue != SWAP_STATUS_ORIGIN) {
            swapBackToOrigin();
        }
    }

    private void applyAnimation(float fromAbsoluteX, float toAbsoluteX){
        Animation ani = new TranslateAnimation(
                fromAbsoluteX, toAbsoluteX, 0.0f, 0.0f);
        ani.setDuration(500);
        ani.setInterpolator(new DecelerateInterpolator());
        ani.setFillAfter(false);
        contentView.startAnimation(ani);
    }

    public int getSwapStatue() {
        return swapStatue;
    }

    public void setSwapStatue(int swapStatue) {
        this.swapStatue = swapStatue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        leftSwapMenuView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
        rightSwapMenuView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        contentView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        leftSwapMenuView.layout(0, 0, swapMoveDistance, contentView.getMeasuredHeight());
        rightSwapMenuView.layout(getMeasuredWidth() - swapMoveDistance, 0,
                getMeasuredWidth(), contentView.getMeasuredHeight());
    }

}
