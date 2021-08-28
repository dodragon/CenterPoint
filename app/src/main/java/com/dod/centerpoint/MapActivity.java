package com.dod.centerpoint;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dod.centerpoint.data.LocationData;
import com.dod.centerpoint.util.GpsTracker;
import com.dod.centerpoint.util.dialog.Loading;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.List;

public class MapActivity extends AppCompatActivity implements View.OnClickListener{

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        checkPermission();
    }

    private void init(){
        Loading loading = new Loading(this);
        loading.show();
        mapView = new MapView(this);
        ((ViewGroup)findViewById(R.id.map_view)).addView(mapView);
        setCurrentLocation();
        setOnClick();
        loading.dismiss();
    }

    private void setCurrentLocation(){
        Location currentLocation = new GpsTracker(this).getLocation();
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(currentLocation.getLatitude(), currentLocation.getLongitude()), true);
    }

    private void checkPermission(){
        TedPermission.create()
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        init();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(MapActivity.this, "설정에서 위치 권한 승인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .setDeniedMessage("위치 권한을 설정해야 지도찾기가 가능합니다.\n설정으로 이동해서 위치권한을 승인해주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    private void setOnClick(){
        int[] ids = new int[]{
                R.id.back,
                R.id.move_current,
                R.id.select
        };

        for(int id : ids){
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            onBackPressed();
        }else if(v.getId() == R.id.move_current){
            setCurrentLocation();
        }else if(v.getId() == R.id.select){
            findAddress();
        }
    }

    private void findAddress(){
        new MapReverseGeoCoder(getString(R.string.kakao_app_key), mapView.getMapCenterPoint(), new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
            @Override
            public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
                new AlertDialog.Builder(MapActivity.this)
                        .setTitle("지도찾기")
                        .setMessage(s + "\n위 주소로 선택하시겠습니까?")
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setMainList(s);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }

            @Override
            public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
                new AlertDialog.Builder(MapActivity.this)
                        .setTitle("지도찾기")
                        .setMessage("선택하신 위치에 주소를 찾을 수 없습니다.\n하지만 주소 없이도 센터찾기는 가능합니다.\n'주소없음'으로 해당 위치를 선택 하시겠습니까?")
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setMainList("주소없음 (" + mapView.getMapCenterPoint().getMapPointGeoCoord().longitude + ", "
                                        + mapView.getMapCenterPoint().getMapPointGeoCoord().latitude + ")");
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        }, this).startFindingAddress();
    }

    private void setMainList(String address){
        LocationData locationData = new LocationData();
        locationData.setMainAddress(address);
        locationData.setLongitude(mapView.getMapCenterPoint().getMapPointGeoCoord().longitude);
        locationData.setLatitude(mapView.getMapCenterPoint().getMapPointGeoCoord().latitude);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("addressData", locationData);
        intent.putExtra("type", "map");
        setResult(RESULT_OK, intent);
        finish();
    }
}