package com.example.paul.swaplistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwapableListView listView = (SwapableListView) findViewById(R.id.swappableListView);
        List<String> lists = new ArrayList<>();
        for (int i = 0 ; i < 20 ; i ++){
            lists.add("" + i);
        }
        BaseAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.listview_single, R.id.name,lists);
        listView.setAdapter(new SwapableAdapter<>(getApplicationContext(), adapter, lists));
    }
}
