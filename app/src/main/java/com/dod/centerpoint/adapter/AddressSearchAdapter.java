package com.dod.centerpoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dod.centerpoint.R;
import com.dod.centerpoint.data.KakaoAddressDoc;
import com.dod.centerpoint.holder.AddressSearchHolder;
import com.dod.centerpoint.holder.MainHolder;

import java.util.ArrayList;
import java.util.List;

public class AddressSearchAdapter extends RecyclerView.Adapter<AddressSearchHolder> {

    Context context;
    List<KakaoAddressDoc> list;

    public AddressSearchAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public AddressSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.item_address_search, parent, false) ;
        return new AddressSearchHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressSearchHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addList(List<KakaoAddressDoc> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clearList(){
        list.clear();
    }
}
