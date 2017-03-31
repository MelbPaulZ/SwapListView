package com.example.paul.swaplistview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 30/3/17.
 */

public class SwapMenu {

    private List<SwapMenuItem> menuItems;
    private Context context;

    public SwapMenu(Context context) {
        this.context = context;
        menuItems = new ArrayList<>();
    }

    public List<SwapMenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<SwapMenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void addMenuItems(SwapMenuItem menuItem){
        menuItems.add(menuItem);
    }

    public void removeMenuItem(SwapMenuItem menuItem){
        menuItems.remove(menuItem);
    }


}
