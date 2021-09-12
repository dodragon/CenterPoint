package com.dod.centerpoint.util;

import android.graphics.PointF;

import com.dod.centerpoint.data.LocationData;

import java.util.ArrayList;
import java.util.List;

public class Centroid {

    public LocationData getCenterPoint(List<LocationData> list){
        if(list.size() == 2){
            return centerOfTwin(list);
        }else {
            return getPolygonCentroid(list);
        }
    }

    public LocationData getPolygonCentroid(List<LocationData> pointList){
        double centerX = 0;
        double centerY = 0;
        double polygonArea = 0;

        int firstIndex;
        int secondIndex;
        int sourceCnt = pointList.size();

        LocationData firstPoint;
        LocationData secondPoint;

        double facter;
        for(firstIndex = 0;firstIndex<sourceCnt;firstIndex++){
            secondIndex = (firstIndex + 1) % sourceCnt;

            firstPoint = pointList.get(firstIndex);
            secondPoint = pointList.get(secondIndex);

            facter =  (firstPoint.getLongitude() * secondPoint.getLatitude()) - (secondPoint.getLongitude() * firstPoint.getLatitude());

            polygonArea += facter;

            centerX += (firstPoint.getLongitude() + secondPoint.getLongitude()) * facter;
            centerY += (firstPoint.getLatitude() + secondPoint.getLatitude()) * facter;
        }

        polygonArea /= 2;
        polygonArea *= 6;

        facter = 1 / polygonArea;

        centerX *= facter;
        centerY *= facter;

        LocationData result = new LocationData();
        result.setLongitude(centerX);
        result.setLatitude(centerY);
        return result;
    }

    private LocationData centerOfTwin(List<LocationData> list){
        ArrayList<PointF> mVertexs = new ArrayList<>();
        for(LocationData data : list){
            mVertexs.add(new PointF((float)data.getLongitude(), (float)data.getLatitude()));
        }

        double x = (mVertexs.get(0).x + mVertexs.get(1).x)/2;
        double y = (mVertexs.get(0).y + mVertexs.get(1).y)/2;

        LocationData centerPoint = new LocationData();
        centerPoint.setLatitude(y);
        centerPoint.setLongitude(x);
        return centerPoint;
    }
}
