package com.example.paul.swaplistview;

import android.util.Log;
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
    private SwapMenuItemClickListener itemClickListener;
    private SwapableListView listView;
    private int position;


    public SwapMenuView(SwapMenu swapMenu){
        super(swapMenu.getContext());
        this.swapMenu = swapMenu;
        initMenuItems();
    }

    private void initMenuItems(){
        List<SwapMenuItem> menuItems = swapMenu.getMenuItems();
        for(int i = 0; i < menuItems.size(); i++){
            addItem(menuItems.get(i), i);
        }
    }

    private void addItem(SwapMenuItem swapMenuItem, int id){
        LayoutParams params = new LayoutParams(swapMenuItem.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(id);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        parent.setBackground(swapMenuItem.getBackground());
        addView(parent);

    }

    public SwapMenuItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(SwapMenuItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener!=null && swapLayout.isOpen()){
            itemClickListener.onSwapMenuItemClick(this, swapMenu, position);
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SwapLayout getSwapLayout() {
        return swapLayout;
    }

    public void setSwapLayout(SwapLayout swapLayout) {
        this.swapLayout = swapLayout;
    }

    public interface SwapMenuItemClickListener {
        void onSwapMenuItemClick(SwapMenuView swapMenuView, SwapMenu swapMenu, int index);
    }
}
