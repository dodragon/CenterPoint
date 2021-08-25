package com.dod.centerpoint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dod.centerpoint.adapter.MainAdapter;
import com.dod.centerpoint.data.LocationData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

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

        setOnClick();
    }

    private void setOnClick(){
        int[] ids = new int[]{
                R.id.address_search,
        };

        for(int id : ids){
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.address_search){
            resultLauncher.launch(new Intent(MainActivity.this, AddressSearch.class));
        }
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Intent getData = result.getData();
                        if(getData.getStringExtra("type").equals("address")){
                            addList((LocationData) getData.getSerializableExtra("addressData"));
                        }else{
                           //TODO: 지도찾기 리스트 추가
                        }
                    }
                }
            }
    );

    private void addList(LocationData data){
        adapter.addList(data);
    }

    private List<LocationData> getList(){
        return adapter.getList();
    }
}