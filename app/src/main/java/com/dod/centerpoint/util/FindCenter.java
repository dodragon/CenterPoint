package com.dod.centerpoint.util;

import android.graphics.PointF;

import com.dod.centerpoint.data.LocationData;

import java.util.ArrayList;

public class FindCenter {

    private ArrayList<PointF> mVertexs = new ArrayList<>();

    public FindCenter() {
        mVertexs = new ArrayList<>();
    }

    public void addPoint(float xPos, float yPos) {
        mVertexs.add(new PointF(xPos, yPos));
    }

    public void deletePoint(int position){
        mVertexs.remove(position);
    }

    public void clear() {
        mVertexs.clear();
    }

    public LocationData getCenter(){
        if(mVertexs.size() > 2){
            return centroidOfPolygon();
        }else if(mVertexs.size() == 2){
            return centerOfTwin();
        }else {
            return null;
        }
    }

    private LocationData centroidOfPolygon() {
        double centerX = 0, centerY = 0;
        double area = 0;

        LocationData centerPoint = new LocationData();
        int firstIndex, secondIndex, sizeOfVertexs = mVertexs.size();

        PointF	firstPoint;
        PointF 	secondPoint;

        double factor = 0;

        for (firstIndex = 0; firstIndex < sizeOfVertexs; firstIndex++) {
            secondIndex = (firstIndex + 1) % sizeOfVertexs;

            firstPoint 	= mVertexs.get(firstIndex);
            secondPoint	= mVertexs.get(secondIndex);

            factor = ((firstPoint.x * secondPoint.y) - (secondPoint.x * firstPoint.y));

            area += factor;

            centerX += (firstPoint.x + secondPoint.x) * factor;
            centerY += (firstPoint.y + secondPoint.y) * factor;
        }

        area /= 2.0;
        area *= 6.0f;

        factor = 1 / area;

        centerX *= factor;
        centerY *= factor;

        centerPoint.setLatitude(centerX);
        centerPoint.setLongitude(centerY);
        return centerPoint;
    }

    private LocationData centerOfTwin(){
        double x = (mVertexs.get(0).x + mVertexs.get(1).x)/2;
        double y = (mVertexs.get(0).y + mVertexs.get(1).y)/2;

        LocationData centerPoint = new LocationData();
        centerPoint.setLatitude(x);
        centerPoint.setLongitude(y);
        return centerPoint;
    }
}
