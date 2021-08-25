package com.dod.centerpoint.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dod.centerpoint.MainActivity;
import com.dod.centerpoint.R;
import com.dod.centerpoint.data.KakaoAddressDoc;
import com.dod.centerpoint.data.KakaoRoadAddress;
import com.dod.centerpoint.data.LocationData;

public class AddressSearchHolder extends RecyclerView.ViewHolder {

    private Context context;


    public AddressSearchHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    public void setData(KakaoAddressDoc data){
        ((TextView)itemView.findViewById(R.id.address)).setText(data.getAddressName());
        itemView.findViewById(R.id.choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("addressData", setLocationData(data.getRoadAddress()));
                intent.putExtra("type", "address");
                ((Activity)context).setResult(Activity.RESULT_OK, intent);
                ((Activity)context).finish();
            }
        });
    }

    private LocationData setLocationData(KakaoRoadAddress data){
        LocationData locationData = new LocationData();
        locationData.setMainAddress(data.getAddressName());
        locationData.setLongitude(Double.parseDouble(data.getX()));
        locationData.setLatitude(Double.parseDouble(data.getY()));
        return locationData;
    }
}
