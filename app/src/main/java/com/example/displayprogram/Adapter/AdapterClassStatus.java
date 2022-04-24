package com.example.displayprogram.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.displayprogram.Model.ModelClass;
import com.example.displayprogram.R;
import com.example.displayprogram.Utils.CommonFunction;

import java.util.ArrayList;

public class AdapterClassStatus extends RecyclerView.Adapter<AdapterClassStatus.ViewHolder> {
    private final ArrayList<ModelClass> listData;

    // RecyclerView recyclerView;
    public AdapterClassStatus(ArrayList<ModelClass> listdata) {
        this.listData = listdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.class_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelClass myListData = listData.get(position);

            holder.tvMessage.setText(myListData.getUnitcode() + ", " + myListData.getUnitname() + " - " + CommonFunction.timeConvert(myListData.getStarttime()) + " - " + CommonFunction.timeConvert(myListData.getEndtime()));
            holder.tvStatus.setText(myListData.getSchedulestatus());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMessage, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            this.tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);

        }
    }
}
