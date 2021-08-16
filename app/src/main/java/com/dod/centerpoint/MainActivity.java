package com.dod.centerpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dod.centerpoint.adapter.MainAdapter;
import com.dod.centerpoint.data.LocationData;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList();
    }

    private void initList(){
        RecyclerView recyclerView = findViewById(R.id.recycler);
        adapter = new MainAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void addList(LocationData data){
        adapter.addList(data);
    }

    private List<LocationData> getList(){
        return adapter.getList();
    }
}