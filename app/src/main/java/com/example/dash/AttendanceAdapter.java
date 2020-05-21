package com.example.dash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<ArrayList<String>> dataList;
    AttendanceAdapter(List<ArrayList<String>> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View DataListView = layoutInflater.inflate(R.layout.display_attendance,parent,false);
        AttendanceAdapter.ViewHolder viewHolder;
        viewHolder = new ViewHolder(DataListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, int position) {
        String  Code = dataList.get(position).get(0);
        float hour = Integer.parseInt(dataList.get(position).get(1));
        float present =  Integer.parseInt(dataList.get(position).get(2));
            TextView code = holder.code;
            TextView totHours = holder.totHours;
            TextView totPresent = holder.totPresent;
            TextView precentage = holder.precentage;

            code.setText(Code);
            totHours.setText(String.valueOf((int)hour));
            totPresent.setText(String.valueOf((int)present));
//            precentage.setText((present/hour)*100);
        System.out.println((present/hour));
        precentage.setText(String.valueOf((int)((present/hour)*100.0)));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView code,totHours,totPresent,precentage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.subjectCode);
            totHours = itemView.findViewById(R.id.totalHours);
            totPresent = itemView.findViewById(R.id.totalPresent);
            precentage = itemView.findViewById(R.id.precentage);
        }
    }
}
