package com.dod.centerpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dod.centerpoint.adapter.AddressSearchAdapter;
import com.dod.centerpoint.data.KaKaoAddressData;
import com.dod.centerpoint.http_interface.KakaoInterface;
import com.dod.centerpoint.util.RecyclerviewPagingListener;
import com.dod.centerpoint.util.dialog.Loading;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressSearch extends AppCompatActivity {

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
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setSearchBtn(){
        EditText searchEdt = findViewById(R.id.search);
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchWord = searchEdt.getText().toString();
                    if(searchWord.equals("")){
                        Toast.makeText(AddressSearch.this, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }else {
                        if(adapter != null){
                            adapter.clearList();
                        }
                        searchWord = v.getText().toString();
                        page = 1;
                        searchToKakao(searchWord);
                    }
                    return true;
                }
                return false;
            }
        });
        searchEdt.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
        recyclerView.addOnScrollListener(new RecyclerviewPagingListener() {
            @Override
            public void onScrollChanged(RecyclerView recyclerView, int newState, boolean isAdding) {
                if(page != 0 && !isEnd){
                    searchToKakao(searchWord);
                }
            }
        });
    }

    private void searchToKakao(String query){
        Loading loading = new Loading(this);
        loading.show();
        //noinspection NullableProblems
        kakaoAddressService.searchAddress(getString(R.string.kakao_rest_api_key), query, page, size).enqueue(new Callback<KaKaoAddressData>() {
            @Override
            public void onResponse(Call<KaKaoAddressData> call, Response<KaKaoAddressData> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        KaKaoAddressData data = response.body();

                        if(data.getMeta().getTotalCount() > 0){
                            if(adapter == null){
                                setListAdapter();
                            }
                            adapter.addList(data.getDocuments());
                            if(!data.getMeta().isEnd()){
                                isEnd = false;
                                page++;
                            }else {
                                isEnd = true;
                            }
                        }else {
                            Toast.makeText(AddressSearch.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Log.e("카카오 주소찾기", "응답없음");
                    }
                }else {
                    Log.e("카카오 주소찾기", String.valueOf(response.code()));
                }
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<KaKaoAddressData> call, Throwable t) {
                Log.e("카카오 주소찾기", t.getLocalizedMessage());
                loading.dismiss();
            }
        });
    }
}