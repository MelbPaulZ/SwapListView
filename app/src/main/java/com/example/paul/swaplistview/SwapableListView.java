package com.example.paul.swaplistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Paul on 29/3/17.
 */

public class SwapableListView extends ListView {
    private SwapLayout preSwapedView, curSwapedView;
    private float preX, preY;
    private final int DIRECTION_NONE = -1;
    private final int DIRECTION_VERTICAL = 1;
    private final int DIRECTION_HORIZONTAL = 2;
    private int direction;
    private int scaledTouchSlop;
    private int MAX_X = 20;
    private int MAX_Y = 5;

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
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (curSwapedView!=null && curSwapedView.isOpen()){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int curSwapPosition = pointToPosition((int)ev.getX(), (int)ev.getY());
        if (ev.getAction() == MotionEvent.ACTION_DOWN){ // save curSwapPosition to ensure each time only one item is swapped
            curSwapedView = (SwapLayout) getChildAt(curSwapPosition - getFirstVisiblePosition());
            preX = ev.getX();
            preY = ev.getY();
            if (preSwapedView!=null) {
                if (preSwapedView.equals(curSwapedView)){
                    direction = DIRECTION_VERTICAL;
                    if (curSwapedView.getSwapStatue()!= SwapLayout.SWAP_STATUS_ORIGIN) {
                        if (ev.getX() < SwapLayout.SWAP_DISTANCE){
                            curSwapedView.getLeftSwapMenuView().onClick(curSwapedView.getLeftSwapMenuView());
                        }else if (ev.getX() > getWidth() - SwapLayout.SWAP_DISTANCE){
                            curSwapedView.getRightSwapMenuView().onClick(curSwapedView.getRightSwapMenuView());
                        }
                        curSwapedView.resetToOriginal();
                        return true;
                    }else{
                        curSwapedView.onClickContent(curSwapedView.getContentView());
                    }
                }else{
                    preSwapedView.resetToOriginal(); // change the previous swapped view to original status if previous view is not current view
                }
            }
            if (curSwapedView!=null) { // need to check whether this is null because it might be reused
                curSwapedView.onSwap(ev);
            }
        }else if (ev.getAction() == MotionEvent.ACTION_UP){
            preSwapedView = curSwapedView;
            direction = DIRECTION_NONE;

            if (curSwapedView!=null) { // when swap from another view, this might be null
                curSwapedView.onSwap(ev);
            }


        }else if (ev.getAction() == MotionEvent.ACTION_MOVE){
            if (direction == DIRECTION_VERTICAL){
                if (curSwapedView!=null){
                    curSwapedView.onSwap(ev);
                }
                ev.setAction(MotionEvent.ACTION_CANCEL);
                super.onTouchEvent(ev);
                return true;
            }else if (direction == DIRECTION_NONE){
                float dx = Math.abs(preX - ev.getX());
                float dy = Math.abs(preY - ev.getY());
                if (dx > MAX_X){
                    // this is vertical scroll
                    direction = DIRECTION_VERTICAL;
                }else if (dy > MAX_Y){
                    direction = DIRECTION_HORIZONTAL;
                }

            }
        }

        return super.onTouchEvent(ev);
    }

}
