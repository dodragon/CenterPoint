package com.dod.centerpoint.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KakaoRoadAddress implements Serializable {

    @SerializedName("address_name")
    private String addressName;
    @SerializedName("region_1depth_name")
    private String region1DepthName;
    @SerializedName("region_2depth_name")
    private String region2DepthName;
    @SerializedName("region_3depth_name")
    private String region3DepthName;
    @SerializedName("road_name")
    private String roadName;
    @SerializedName("underground_yn")
    private String underGroundYN;
    @SerializedName("main_building_no")
    private String mainBuildingNo;
    @SerializedName("sub_building_no")
    private String subBuildingNo;
    @SerializedName("building_name")
    private String buildingName;
    @SerializedName("zone_no")
    private String zoneNo;
    @SerializedName("x")
    private String x;
    @SerializedName("y")
    private String y;

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getRegion1DepthName() {
        return region1DepthName;
    }

    public void setRegion1DepthName(String region1DepthName) {
        this.region1DepthName = region1DepthName;
    }

    public String getRegion2DepthName() {
        return region2DepthName;
    }

    public void setRegion2DepthName(String region2DepthName) {
        this.region2DepthName = region2DepthName;
    }

    public String getRegion3DepthName() {
        return region3DepthName;
    }

    public void setRegion3DepthName(String region3DepthName) {
        this.region3DepthName = region3DepthName;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getUnderGroundYN() {
        return underGroundYN;
    }

    public void setUnderGroundYN(String underGroundYN) {
        this.underGroundYN = underGroundYN;
    }

    public String getMainBuildingNo() {
        return mainBuildingNo;
    }

    public void setMainBuildingNo(String mainBuildingNo) {
        this.mainBuildingNo = mainBuildingNo;
    }

    public String getSubBuildingNo() {
        return subBuildingNo;
    }

    public void setSubBuildingNo(String subBuildingNo) {
        this.subBuildingNo = subBuildingNo;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getZoneNo() {
        return zoneNo;
    }

    public void setZoneNo(String zoneNo) {
        this.zoneNo = zoneNo;
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

    @Override
    public String toString() {
        return "KakaoRoadAddress{" +
                "addressName='" + addressName + '\'' +
                ", region1DepthName='" + region1DepthName + '\'' +
                ", region2DepthName='" + region2DepthName + '\'' +
                ", region3DepthName='" + region3DepthName + '\'' +
                ", roadName='" + roadName + '\'' +
                ", underGroundYN='" + underGroundYN + '\'' +
                ", mainBuildingNo='" + mainBuildingNo + '\'' +
                ", subBuildingNo='" + subBuildingNo + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", zoneNo='" + zoneNo + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
