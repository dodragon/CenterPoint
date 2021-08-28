package com.dod.centerpoint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dod.centerpoint.adapter.MainAdapter;
import com.dod.centerpoint.data.LocationData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MainAdapter adapter;
    private long backpressedTime = 0;

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
                R.id.map_search
        };

        for(int id : ids){
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.address_search){
            resultLauncher.launch(new Intent(MainActivity.this, AddressSearch.class));
        }else if(v.getId() == R.id.map_search){
            resultLauncher.launch(new Intent(MainActivity.this, MapActivity.class));
        }
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Intent getData = result.getData();
                        checkDuplicatedData((LocationData) getData.getSerializableExtra("addressData"));
                    }
                }
            }
    );

    private void checkDuplicatedData(LocationData data){
        List<LocationData> list = adapter.getList();

        boolean isOk = true;
        for(LocationData selectedData : list){
            if(selectedData.getMainAddress().equals(data.getMainAddress()) ||
                    (selectedData.getLatitude() == data.getLatitude() && selectedData.getLongitude() == data.getLongitude())){
                Toast.makeText(this, "이미 동일한 위치정보가 설정 되었습니다.", Toast.LENGTH_SHORT).show();
                isOk = false;
                break;
            }
        }

        if(isOk){
            addList(data);
        }
    }

    private void addList(LocationData data){
        adapter.addList(data);
        setListVisible(adapter.getList().size());
    }

    public void setListVisible(int listSize){
        if(listSize > 0){
            findViewById(R.id.recycler).setVisibility(View.VISIBLE);
            findViewById(R.id.none_txt).setVisibility(View.GONE);
        }else {
            findViewById(R.id.recycler).setVisibility(View.GONE);
            findViewById(R.id.none_txt).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }
    }

    //GET KEY HASH
    /*public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}