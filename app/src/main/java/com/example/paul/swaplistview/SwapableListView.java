package com.example.paul.swaplistview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Paul on 29/3/17.
 */

public class SwapableListView extends ListView {
    private SwapLayout preSwapedView, curSwapedView;
    private float startX, startY;
    private final int DIRECTION_NONE = -1;
    private final int DIRECTION_VERTICAL = 1;
    private final int DIRECTION_HORIZONTAL = 2;
    private int direction;
    private int scaledTouchSlop;
    private int MAX_X = 20;
    private int MAX_Y = 5;
    private boolean mIsScrolling = false;
    private int mScrollState;
    private boolean disableNextClickContent = false;
    private boolean disableNextTouchUp = false;

    public SwapableListView(Context context) {
        super(context);
        init();
    }

    public SwapableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwapableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        direction = DIRECTION_NONE;
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mScrollState = scrollState;
                Log.i("tag", "onScrollStateChanged: stateChange" + scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("tag", "onScroll: " + "onScroll");
            }
        });
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(listener);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
            mIsScrolling = false;
            onTouchEvent(ev);
            return super.onInterceptTouchEvent(ev);
        }

        switch (action){
            case MotionEvent.ACTION_MOVE: {
                if (mIsScrolling) {
                    return true;
                }

                int xDiff = calculateDistanceX(ev);
                if (xDiff > scaledTouchSlop) {
                    mIsScrolling = true;
                    curSwapedView.setBeforeX(ev.getX());
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_DOWN:
                if (mScrollState == OnScrollListener.SCROLL_STATE_FLING){
                    disableNextClickContent = true;
                }
                startX = ev.getX();
                startY = ev.getY();
                int curSwapPosition = pointToPosition((int)ev.getX() , (int) ev.getY());
                curSwapedView = (SwapLayout)getChildAt(curSwapPosition - getFirstVisiblePosition());
                if (preSwapedView!=null && preSwapedView.isOpen()){ // if preSwaped is open, then must click one of those three
                    if (ev.getX() < SwapLayout.SWAP_DISTANCE
                            && curSwapedView.getSwapStatue() == SwapLayout.SWAP_STATUS_RIGHT){ // if click left menu and left menu is opened
                        curSwapedView.getLeftSwapMenuView().onClick(curSwapedView.getLeftSwapMenuView());
                    }else if (ev.getX() > getWidth() - SwapLayout.SWAP_DISTANCE
                            && curSwapedView.getSwapStatue() == SwapLayout.SWAP_STATUS_LEFT){ // if click right menu and right menu is opened
                        curSwapedView.getRightSwapMenuView().onClick(curSwapedView.getRightSwapMenuView());
                    }else {
                        preSwapedView.resetToOriginal();
                        disableNextClickContent = true; // unable next onClick content motion
                        disableNextTouchUp = true;
                        return true;
                    }
                    break;
                }

                if (curSwapedView!=null){
                    curSwapedView.onSwap(ev);
                    preSwapedView = curSwapedView; // check if this is right
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action){
            case MotionEvent.ACTION_UP:{
                // if is not a scroll down fling touch, then can be treated as onClick Content
                if (!mIsScrolling
                        && calculateDistanceX(ev) < scaledTouchSlop
                        && calculateDistanceY(ev) < scaledTouchSlop
                        && mScrollState!= OnScrollListener.SCROLL_STATE_FLING
                        && curSwapedView!=null
                        && !disableNextClickContent){
                    curSwapedView.onClickContent(curSwapedView.getContentView());
                }
                disableNextClickContent = false; // if is not a scroll down fling touch, then

                mIsScrolling = false;
                if (curSwapedView!=null){
                    if (disableNextTouchUp){ // the disable next touch up will not let the child view stop animation
                        disableNextTouchUp = false;
                    }else {
                        curSwapedView.onSwap(ev);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if (mIsScrolling){
                    if (curSwapedView!=null){
                        curSwapedView.onSwap(ev);
                    }
                    return true;
                }
                break;
            }case MotionEvent.ACTION_DOWN:{
                ev.setAction(MotionEvent.ACTION_CANCEL);
            }

        }


        return super.onTouchEvent(ev);
//        int curSwapPosition = pointToPosition((int)ev.getX(), (int)ev.getY());
//        if (ev.getAction() == MotionEvent.ACTION_DOWN){ // save curSwapPosition to ensure each time only one item is swapped
//            curSwapedView = (SwapLayout) getChildAt(curSwapPosition - getFirstVisiblePosition());
//            startX = ev.getX();
//            preY = ev.getY();
//            if (preSwapedView!=null) {
//                if (preSwapedView.equals(curSwapedView)){
//                    direction = DIRECTION_VERTICAL;
//                    if (curSwapedView.getSwapStatue()!= SwapLayout.SWAP_STATUS_ORIGIN) {
//                        if (ev.getX() < SwapLayout.SWAP_DISTANCE){
//                            curSwapedView.getLeftSwapMenuView().onClick(curSwapedView.getLeftSwapMenuView());
//                        }else if (ev.getX() > getWidth() - SwapLayout.SWAP_DISTANCE){
//                            curSwapedView.getRightSwapMenuView().onClick(curSwapedView.getRightSwapMenuView());
//                        }
//                        curSwapedView.resetToOriginal();
//                        return true;
//                    }else{
//                        curSwapedView.onClickContent(curSwapedView.getContentView());
//                    }
//                }else{
//                    preSwapedView.resetToOriginal(); // change the previous swapped view to original status if previous view is not current view
//                }
//            }
//            if (curSwapedView!=null) { // need to check whether this is null because it might be reused
//                curSwapedView.onSwap(ev);
//            }
//        }else if (ev.getAction() == MotionEvent.ACTION_UP){
//            preSwapedView = curSwapedView;
//            direction = DIRECTION_NONE;
//            mIsScrolling = false;
//            if (curSwapedView!=null) { // when swap from another view, this might be null
//                curSwapedView.onSwap(ev);
//            }
//
//
//        }else if (ev.getAction() == MotionEvent.ACTION_MOVE){
//            if (direction == DIRECTION_VERTICAL){
//                if (curSwapedView!=null){
//                    curSwapedView.onSwap(ev);
//                }
//                ev.setAction(MotionEvent.ACTION_CANCEL);
//                super.onTouchEvent(ev);
//                return true;
//            }else if (direction == DIRECTION_NONE){
//                float dx = Math.abs(startX - ev.getX());
//                float dy = Math.abs(preY - ev.getY());
//                if (dx > MAX_X){
//                    // this is vertical scroll
//                    direction = DIRECTION_VERTICAL;
//                }else if (dy > MAX_Y){
//                    direction = DIRECTION_HORIZONTAL;
//                }
//
//            }
//        }
//
//        return super.onTouchEvent(ev);
    }

    private int calculateDistanceX(MotionEvent ev){
        return (int) Math.abs(ev.getX() - startX);
    }

    private int calculateDistanceY(MotionEvent ev){
        return (int) Math.abs(ev.getY() - startY);
    }

}
