package com.example.paul.swaplistview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Paul on 30/3/17.
 */

public class SwapMenuView extends LinearLayout implements View.OnClickListener{
    private SwapLayout swapLayout;
    private SwapMenu swapMenu;


    public SwapMenuView(SwapMenu swapMenu){
        super(swapMenu.getContext());
        this.swapMenu = swapMenu;
        initMenuItems();
    }

    private void initMenuItems(){
        List<SwapMenuItem> menuItems = swapMenu.getMenuItems();
        for (SwapMenuItem swapMenuItem: menuItems){
            addItem(swapMenuItem);
        }
    }

    private void addItem(SwapMenuItem swapMenuItem){
        LayoutParams params = new LayoutParams(swapMenuItem.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout parent = new LinearLayout(getContext());
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        parent.setBackground(swapMenuItem.getBackground());
//        parent.setBackgroundColor(Color.BLACK);
        addView(parent);

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }
}
