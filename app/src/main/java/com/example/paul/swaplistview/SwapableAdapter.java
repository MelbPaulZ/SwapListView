package com.example.paul.swaplistview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Paul on 29/3/17.
 */

public class SwapableAdapter<T extends Object> extends BaseAdapter{

    private List<T> dataList;
    private ListAdapter adapter;
    private Context context;
    private MenuInterface menuInterface;

    public SwapableAdapter(Context context, ListAdapter adapter, List<T> dataList) {
        this.context = context;
        this.adapter = adapter;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return adapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return adapter.getItem(position);
    }


    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwapLayout layout = null;
        if (convertView == null){
            final View contentView = adapter.getView(position, convertView, parent);
            SwapMenu leftSwapMenu = createMenu();
            SwapMenu rightSwapMenu = createMenu();
            SwapMenuView leftSwapMenuView = new SwapMenuView(leftSwapMenu);
            leftSwapMenuView.setPosition(position);
            leftSwapMenuView.setItemClickListener(new SwapMenuView.SwapMenuItemClickListener() {
                @Override
                public void onSwapMenuItemClick(SwapMenuView swapMenuView, SwapMenu swapMenu, int index) {
                    Toast.makeText(context, "onClick content swapMenu", Toast.LENGTH_SHORT).show();
                }
            });
            SwapMenuView rightSwapMenuView = new SwapMenuView(rightSwapMenu);
            rightSwapMenuView.setPosition(position);
            rightSwapMenuView.setItemClickListener(new SwapMenuView.SwapMenuItemClickListener() {
                @Override
                public void onSwapMenuItemClick(SwapMenuView swapMenuView, SwapMenu swapMenu, int index) {
                    Toast.makeText(context,"onClick right swap" + index, Toast.LENGTH_SHORT).show();
                }
            });
            layout = new SwapLayout(contentView, leftSwapMenuView, rightSwapMenuView);
            layout.setPosition(position);
            layout.setOnContentClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "onClick Content", Toast.LENGTH_SHORT).show();
                }
            });
            leftSwapMenuView.setSwapLayout(layout);
            rightSwapMenuView.setSwapLayout(layout);
        }else{
            layout = (SwapLayout) convertView;
            // to refresh view. otherwise the order will be messed
            adapter.getView(position, convertView, parent);
        }
        return layout;
    }

    private SwapMenu createMenu(){
        SwapMenu swapMenu = new SwapMenu(context);
        SwapMenuItem menuItem = new SwapMenuItem(context);
        menuItem.setTitle("t1");
        menuItem.setBackground(new ColorDrawable(Color.GRAY));
        menuItem.setWidth(300);
        swapMenu.addMenuItems(menuItem);
        return swapMenu;
    }



    public interface MenuInterface{
        void createInView(View v);
        View.OnClickListener leftSwapMenuClickListener();
    }

}
