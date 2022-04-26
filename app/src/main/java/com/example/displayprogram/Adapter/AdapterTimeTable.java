package com.example.displayprogram.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
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

public class AdapterTimeTable extends RecyclerView.Adapter<AdapterTimeTable.ViewHolder> {
    private final ArrayList<ModelClass> listData;

    // RecyclerView recyclerView;
    public AdapterTimeTable(ArrayList<ModelClass> listdata) {
        this.listData = listdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.timetable_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ModelClass myListData = listData.get(position);
        Log.e("Adapter : ","date : "+myListData.getTransactiondate());
        holder.tvInfo.setText(myListData.getUnitcode() + ", " + myListData.getUnitname());
        holder.tvClassId.setText(myListData.getClassno());
        holder.tvDate.setText(CommonFunction.getDateInDDMMMMYYYY(myListData.getTransactiondate()));
        holder.tvTime.setText(CommonFunction.timeConvert(myListData.getStarttime() )+ " - " + CommonFunction.timeConvert(myListData.getEndtime()));
        holder.tvTeacherName.setText(myListData.getTeachername());
        holder.tvRemarks.setText(myListData.getSchedulestatus());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvInfo, tvClassId, tvDate, tvTime, tvTeacherName,tvRemarks;

        public ViewHolder(View itemView) {
            super(itemView);

            tvInfo = itemView.findViewById(R.id.tvInfo);
            tvClassId = itemView.findViewById(R.id.tvClassId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            tvRemarks = itemView.findViewById(R.id.tvRemarks);

        }
    }
}
