package com.example.displayprogram.Network.Response;

import com.example.displayprogram.Model.ModelClass;

import java.util.ArrayList;

public class TimeTableResponse {
    private ArrayList<ModelClass> listTimeTable;

    public ArrayList<ModelClass> getListTimeTable() {
        return listTimeTable;
    }

    public void setListTimeTable(ArrayList<ModelClass> listTimeTable) {
        this.listTimeTable = listTimeTable;
    }
}
