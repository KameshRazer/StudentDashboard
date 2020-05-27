package com.example.dash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TotalMarkAdapter extends RecyclerView.Adapter<TotalMarkAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> dataList;
    TotalMarkAdapter(ArrayList<ArrayList<String >> dataList){
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public TotalMarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View DataListView = layoutInflater.inflate(R.layout.display_total_marks,parent,false);
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(DataListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TotalMarkAdapter.ViewHolder holder, int position) {
        holder.result.setText(dataList.get(position).get(1));
        holder.marks.setText(dataList.get(position).get(0));
        holder.subject.setText(dataList.get(position).get(2));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView subject,marks,result;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subjectName);
            marks = itemView.findViewById(R.id.totalmark);
            result = itemView.findViewById(R.id.result);
        }
    }
}
