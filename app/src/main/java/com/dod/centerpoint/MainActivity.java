package com.dod.centerpoint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dod.centerpoint.adapter.MainAdapter;
import com.dod.centerpoint.data.LocationData;
import com.dod.centerpoint.util.Centroid;
import com.dod.centerpoint.util.dialog.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MainAdapter adapter;
    private long backpressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList();

        if(getIntent().getData() != null){
            Loading loading = new Loading(this);
            loading.show();

            String docId = getIntent().getData().toString().split("docId=")[1];

            DocumentReference ref = FirebaseFirestore.getInstance().collection("location").document(docId);
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot snapshot = task.getResult();
                        Map<String, Object> data = snapshot.getData();

                        Gson gson = new Gson();
                        for(int i=0;i<data.size();i++){
                            adapter.addList(gson.fromJson(data.get(String.valueOf(i)).toString(), new TypeToken<LocationData>(){}.getType()));
                        }

                        loading.dismiss();
                        findViewById(R.id.find_center).performClick();
                    }else {
                        Toast.makeText(MainActivity.this, "데이터를 읽어오는데 실패했네요 ㅈㅅ..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
                R.id.map_search,
                R.id.find_center
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
        }else if(v.getId() == R.id.find_center){
            Loading loading = new Loading(this);
            loading.show();
            ArrayList<LocationData> pointList = adapter.getList();
            Log.e("로케이션 리스트", pointList.toString());
            if(pointList.size() <= 1){
                Toast.makeText(this, "최소 2개 이상의 좌표가 있어야 해요 !", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }else{
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                LocationData center = new Centroid().getCenterPoint(pointList);

                Map<String, String> data = new HashMap<>();
                for(int i=0;i<pointList.size();i++){
                    data.put(String.valueOf(i), new Gson().toJson(pointList.get(i)));
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("location")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                intent.putExtra("docId", documentReference.getId());
                                intent.putExtra("locationList", pointList);
                                intent.putExtra("center", center);
                                loading.dismiss();
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "다시 시도해 주세요 !", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        });
            }
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