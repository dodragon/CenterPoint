package com.dod.centerpoint.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class KaKaoAddressData implements Serializable {

    @SerializedName("meta")
    private KakaoMeta meta;
    @SerializedName("documents")
    private List<KakaoAddressDoc> documents;

    public KakaoMeta getMeta() {
        return meta;
    }

    public void setMeta(KakaoMeta meta) {
        this.meta = meta;
    }

    public List<KakaoAddressDoc> getDocuments() {
        return documents;
    }

    public void setDocuments(List<KakaoAddressDoc> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "KaKaoAddressData{" +
                "meta=" + meta +
                ", documents=" + documents +
                '}';
    }
}
