package com.dod.centerpoint.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dod.centerpoint.R;
import com.dod.centerpoint.adapter.MainAdapter;
import com.dod.centerpoint.data.LocationData;

public class MainHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final Context context;
    private final MainAdapter adapter;
    private int position;

    public MainHolder(@NonNull View itemView, Context context, MainAdapter adapter) {
        super(itemView);
        this.context = context;
        this.adapter = adapter;
    }

    public void setData(LocationData data, int position){
        this.position = position;
        ((TextView)itemView.findViewById(R.id.main_address)).setText(data.getMainAddress());
        ((TextView)itemView.findViewById(R.id.detail_address)).setText(data.getDetailAddress());
        itemView.findViewById(R.id.delete).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        adapter.deleteList(position);
    }
}
