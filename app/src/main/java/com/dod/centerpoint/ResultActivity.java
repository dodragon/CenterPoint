package com.dod.centerpoint;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dod.centerpoint.data.LocationData;
import com.dod.centerpoint.util.Distance;
import com.dod.centerpoint.util.dialog.Loading;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.util.KakaoCustomTabsClient;
import com.kakao.sdk.link.LinkClient;
import com.kakao.sdk.link.WebSharerClient;
import com.kakao.sdk.link.model.LinkResult;
import com.kakao.sdk.template.model.Button;
import com.kakao.sdk.template.model.Content;
import com.kakao.sdk.template.model.FeedTemplate;
import com.kakao.sdk.template.model.Link;
import com.kakao.sdk.template.model.Social;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private Loading loading;
    private MapView mapView;

    private int centerPoiTag;
    private String centerAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        KakaoSdk.init(this, "a37f49798ce5901bf74708a91922a099");

        checkPermission();
    }

    @SuppressWarnings("unchecked")
    private void getData(){
        loading = new Loading(this);
        loading.show();
        Intent getData = getIntent();
        setView(
                (ArrayList<LocationData>)getData.getSerializableExtra("locationList"),
                (LocationData)getData.getSerializableExtra("center")
        );
    }

    private void setView(ArrayList<LocationData> locationList, LocationData center){
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.center_address).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);

        centerPoiTag = locationList.size();

        mapView = new MapView(this);
        ((ViewGroup)findViewById(R.id.map_view)).addView(mapView);

        setDistance(locationList, center);

        for(int i=0;i<locationList.size();i++){
            LocationData data = locationList.get(i);
            MapPOIItem poi = new MapPOIItem();
            poi.setItemName(i + 1 + "번째 포인트!");
            poi.setTag(i);
            poi.setMapPoint(MapPoint.mapPointWithGeoCoord(data.getLatitude(), data.getLongitude()));
            poi.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poi.setCustomImageResourceId(R.drawable.ic_location_pin);
            poi.setCustomImageAutoscale(true);
            poi.setCustomImageAnchor(0.5f, 1.0f);
            mapView.addPOIItem(poi);
        }

        setCenterPoi(center);
        setLine(locationList, center);
        findAddress(center);

        loading.dismiss();
    }


    private void setDistance(List<LocationData> list, LocationData center){
        StringBuilder distanceTxt = new StringBuilder();
        for(int i=0;i<list.size();i++){
            distanceTxt
                    .append("* ")
                    .append(i + 1)
                    .append("번(")
                    .append(list.get(i).getMainAddress())
                    .append("): ")
                    .append(getDistanceTxt(new Distance(list.get(i).getLongitude(), list.get(i).getLatitude(), center.getLongitude(), center.getLatitude()).distance()));
            if(i + 1 != list.size()){
                distanceTxt.append("\n");
            }
        }

        ((TextView)findViewById(R.id.distance)).setText(distanceTxt);
    }

    @SuppressLint("DefaultLocale")
    private String getDistanceTxt(double distance){
        String disTxt;
        if(distance >= 1000){
            disTxt = String.format("%.2f", distance / 1000) + "km";
        }else{
            disTxt = String.format("%.2f", distance) + "m";
        }
        return disTxt;
    }

    private void checkPermission(){
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        getData();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(ResultActivity.this, "설정에서 위치 권한 승인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .setDeniedMessage("위치 권한을 설정해야 지도찾기가 가능합니다.\n설정으로 이동해서 위치권한을 승인해주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    private void findAddress(LocationData data){
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(data.getLatitude(), data.getLongitude());
        new MapReverseGeoCoder(getString(R.string.kakao_app_key), mapPoint, new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
            @Override
            public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
                centerAddress = s;
                data.setMainAddress("센터 주소 : " + s + "(복사하기)");
                ((TextView)findViewById(R.id.center_address)).setText(data.getMainAddress());
            }

            @Override
            public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
                centerAddress = "";
                data.setMainAddress("센터의 주소를 찾을 수 없어요ㅜㅜ");
                ((TextView)findViewById(R.id.center_address)).setText(data.getMainAddress());
            }
        }, this).startFindingAddress();
    }

    private void setCenterPoi(LocationData data){
        MapPOIItem poi = new MapPOIItem();
        poi.setItemName("여기가 센터!");
        poi.setTag(centerPoiTag);
        poi.setMapPoint(MapPoint.mapPointWithGeoCoord(data.getLatitude(), data.getLongitude()));
        poi.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        poi.setCustomImageResourceId(R.drawable.ic_location_pin);
        poi.setCustomImageAutoscale(true);
        poi.setCustomImageAnchor(0.5f, 1.0f);
        mapView.addPOIItem(poi);
    }

    private void setLine(List<LocationData> list, LocationData center){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MapPoint centerPoint = MapPoint.mapPointWithGeoCoord(center.getLatitude(), center.getLongitude());
                for(int i=0;i<list.size();i++){
                    MapPolyline polyline = new MapPolyline();
                    MapPoint listPoint = MapPoint.mapPointWithGeoCoord(list.get(i).getLatitude(), list.get(i).getLongitude());
                    polyline.addPoint(listPoint);
                    polyline.addPoint(centerPoint);
                    mapView.addPolyline(polyline);
                }

                mapView.moveCamera(CameraUpdateFactory.newMapPoint(centerPoint, 4));
            }
        });
    }

    private void kakaoShare(){
        Map<String, String> data = new HashMap<>();
        data.put("docId", getIntent().getStringExtra("docId"));

        Link link = new Link("https://play.google.com/store/apps/details?id=com.dod.centerpoint",
                "https://play.google.com/store/apps/details?id=com.dod.centerpoint", data);

        Content content = new Content("친구가 여기서 보쟤요!", "https://firebasestorage.googleapis.com/v0/b/center-point-e3b9b.appspot.com/o/KakaoTalk_20210926_174629057.png?alt=media&token=8add0f59-223b-441c-85f2-f901f485dc40",
                link, "눌러서 확인하세요!");
        Social social = new Social();
        Button button = new Button("확인하기", link);

        List<Button> btnList = new ArrayList<>();
        btnList.add(button);


        FeedTemplate template = new FeedTemplate(content, social, btnList);

        if(LinkClient.getInstance().isKakaoLinkAvailable(this)){
            LinkClient.getInstance().defaultTemplate(this, template, null, new Function2<LinkResult, Throwable, Unit>() {
                @Override
                public Unit invoke(LinkResult linkResult, Throwable throwable) {
                    if(throwable != null){
                        Toast.makeText(ResultActivity.this, "공유하기에 실패 했어요ㅜㅜ", Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(linkResult.getIntent());
                    }
                    return null;
                }
            });
        }else {
            Uri uri = WebSharerClient.getInstance().defaultTemplateUri(template);

            try{
                KakaoCustomTabsClient.INSTANCE.openWithDefault(this, uri);
            }catch (UnsupportedOperationException e){
                Toast.makeText(this, "공유 할 수 있는 브라우저가 없습니다ㅜㅜ", Toast.LENGTH_SHORT).show();
            }

            try{
                KakaoCustomTabsClient.INSTANCE.open(this, uri);
            }catch (ActivityNotFoundException e){
                Toast.makeText(this, "공유 할 수 있는 브라우저가 없습니다ㅜㅜ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            onBackPressed();
        }else if(v.getId() == R.id.share){
            kakaoShare();
        }else if(v.getId() == R.id.center_address){
            if(((TextView)v).getText().toString().contains("복사하기") && !centerAddress.equals("")){
                ClipboardManager manager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("센터주소", centerAddress);
                manager.setPrimaryClip(clip);
                Toast.makeText(this, "주소를 복사했어요 !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}