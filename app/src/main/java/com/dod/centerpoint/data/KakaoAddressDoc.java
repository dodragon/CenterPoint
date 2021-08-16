package com.dod.centerpoint.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KakaoAddressDoc implements Serializable {

    @SerializedName("address_name")
    private String addressName;
    @SerializedName("address_type")
    private String addressType;
    @SerializedName("x")
    private String x; //경도(longitude)
    @SerializedName("y")
    private String y; //위도(latitude)
    @SerializedName("road_address")
    private KakaoRoadAddress roadAddress;

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public KakaoRoadAddress getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(KakaoRoadAddress roadAddress) {
        this.roadAddress = roadAddress;
    }

    @Override
    public String toString() {
        return "KakaoAddressDoc{" +
                "addressName='" + addressName + '\'' +
                ", addressType='" + addressType + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", roadAddress=" + roadAddress +
                '}';
    }
}
