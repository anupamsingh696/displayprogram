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
        Log.e("Adapter : ", "date : " + myListData.getTransactiondate());
        holder.tvDate.setText(CommonFunction.getDateInDDMMYYYY(myListData.getTransactiondate()));
        holder.tvStartTime.setText(CommonFunction.timeConvert(myListData.getStarttime()));
        holder.tvEndTime.setText(CommonFunction.timeConvert(myListData.getEndtime()));
        holder.tvClassId.setText(myListData.getClassno());
        holder.tvUnitCode.setText(myListData.getUnitcode());
        holder.tvUnitDesc.setText(myListData.getUnitname());
        holder.tvTeacherName.setText(myListData.getTeachername());
        holder.tvRemarks.setText(myListData.getSchedulestatus());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate, tvStartTime, tvEndTime, tvClassId, tvUnitCode, tvUnitDesc, tvTeacherName, tvRemarks;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvClassId = itemView.findViewById(R.id.tvClassId);
            tvUnitCode = itemView.findViewById(R.id.tvUnitCode);
            tvUnitDesc = itemView.findViewById(R.id.tvUnitDesc);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            tvRemarks = itemView.findViewById(R.id.tvRemarks);

        }
    }
}
