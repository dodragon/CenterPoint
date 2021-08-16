package com.dod.centerpoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dod.centerpoint.R;
import com.dod.centerpoint.data.LocationData;
import com.dod.centerpoint.holder.MainHolder;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainHolder> {

    private final Context context;
    private final List<LocationData> list;
    private MainHolder holder;

    public MainAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.item_main_location_choose, parent, false) ;
        return new MainHolder(view, context, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        holder.setData(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addList(LocationData data){
        this.list.add(data);
        notifyDataSetChanged();
    }

    public void deleteList(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    public void clearList(boolean isDataChange){
        list.clear();
        if(isDataChange){
            notifyDataSetChanged();
        }
    }

    public List<LocationData> getList(){
        return list;
    }
}
