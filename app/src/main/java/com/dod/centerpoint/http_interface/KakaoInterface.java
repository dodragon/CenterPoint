package com.dod.centerpoint.http_interface;

import com.dod.centerpoint.data.KaKaoAddressData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KakaoInterface {

    @GET("/v2/local/search/address.json")
    Call<KaKaoAddressData> searchAddress(
            @Query("query")String query,
            @Query("page")int page,
            @Query("size")int size
    );
}
