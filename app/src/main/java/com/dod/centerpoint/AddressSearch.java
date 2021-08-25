package com.dod.centerpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dod.centerpoint.adapter.AddressSearchAdapter;
import com.dod.centerpoint.data.KaKaoAddressData;
import com.dod.centerpoint.http_interface.KakaoInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("NullableProblems")
public class AddressSearch extends AppCompatActivity {

    //TODO: 주소검색 더보기 구현 필요

    private KakaoInterface kakaoAddressService;
    private int page = 1;

    private static final int size = 20;

    private String searchWord = "";
    private boolean isEnd = false;

    private AddressSearchAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);

        kakaoAddressService = initHttp().create(KakaoInterface.class);
        setSearchBtn();
    }

    private void setSearchBtn(){
        EditText searchEdt = findViewById(R.id.search);
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchWord = searchEdt.getText().toString();
                    if(isEnd){
                        Toast.makeText(AddressSearch.this, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        if(searchWord.equals("")){
                            Toast.makeText(AddressSearch.this, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        }else if(searchWord.equals(v.getText().toString())){
                            page++;
                            searchToKakao(searchWord);
                        }else {
                            searchWord = v.getText().toString();
                            page = 1;
                            searchToKakao(searchWord);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private Retrofit initHttp(){
        return new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void setListAdapter(){
        RecyclerView recyclerView = findViewById(R.id.recycler);
        adapter = new AddressSearchAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void searchToKakao(String query){
        kakaoAddressService.searchAddress(getString(R.string.kakao_rest_api_key), query, page, size).enqueue(new Callback<KaKaoAddressData>() {
            @Override
            public void onResponse(Call<KaKaoAddressData> call, Response<KaKaoAddressData> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        KaKaoAddressData data = response.body();

                        if(adapter == null){
                            setListAdapter();
                        }
                        assert adapter != null;
                        adapter.addList(data.getDocuments());
                        if(!data.getMeta().isEnd()){
                            isEnd = false;
                            page++;
                        }else {
                            isEnd = true;
                        }
                    }else {
                        Log.e("카카오 주소찾기", "응답없음");
                    }
                }else {
                    Log.e("카카오 주소찾기", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<KaKaoAddressData> call, Throwable t) {
                Log.e("카카오 주소찾기", t.getLocalizedMessage());
            }
        });
    }
}