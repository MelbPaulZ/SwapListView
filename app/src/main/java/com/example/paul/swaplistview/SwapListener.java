package com.example.paul.swaplistview;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Paul on 29/3/17.
 */

public class SwapListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;
    private Context context;
    private View v;

    public SwapListener(Context context) {
        this.context = context;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.v = v;
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener implements GestureDetector.OnGestureListener{

        private String TAG = "GestureListener";
        @Override
        public boolean onDown(MotionEvent e) {
            // this has to be true so onFling can be called
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.i(TAG, "onShowPress: " + "on Press");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i(TAG, "onSingleTapUp: " + "on single tap up");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            Log.i(TAG, "onScroll: "  + distanceX + "onScroll:" + distanceY);

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i(TAG, "onLongPress: " + "on long press");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i(TAG, "onFling: ");
            return false;
        }
    }
}
