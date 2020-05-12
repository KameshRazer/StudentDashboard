package com.example.dash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.ViewHolder> {


    private List<ArrayList<String>> dataList;
    MarkAdapter(List<ArrayList<String>> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View DataListView = layoutInflater.inflate(R.layout.display,parent,false);
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(DataListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MarkAdapter.ViewHolder viewHolder, int position) {
//        System.out.println(dataList.size());
        ArrayList<String> data = dataList.get(position);
        int j=0;
        for (int i = 0; i <data.size() ; i+=2) {
            if(data.get(i).equals("Subject")){
                TextView head = viewHolder.head;
                head.setText(data.get(i+1));
            }else
            {
                TextView title = viewHolder.title[j];
                TextView result = viewHolder.result[j];
                title.setText(data.get(i));
                result.setText(data.get(i+1));
                title.setVisibility(View.VISIBLE);
                result.setVisibility(View.VISIBLE);
                j++;
            }
        }
        for (int i = j; i < 6 ; i++) {
            TextView title = viewHolder.title[j];
            TextView result = viewHolder.result[j];
            title.setVisibility(View.GONE);
            result.setVisibility(View.GONE);
            j++;
        }
//        TextView key = viewHolder.key;
//        TextView value = viewHolder.value;
//        System.out.println(data);
//        key.setText(data.get(0));
//        value.setText(data.get(1));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView head;
        TextView[] title = new TextView[6];
        TextView[] result = new TextView[6];

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.title);
            title[0] = itemView.findViewById(R.id.title1);
            result[0] = itemView.findViewById(R.id.result1);
            title[1] = itemView.findViewById(R.id.title2);
            result[1] = itemView.findViewById(R.id.result2);
            title[2]= itemView.findViewById(R.id.title3);
            result[2] = itemView.findViewById(R.id.result3);
            title[3]= itemView.findViewById(R.id.title4);
            result[3] = itemView.findViewById(R.id.result4);
            title[4]= itemView.findViewById(R.id.title5);
            result[4] = itemView.findViewById(R.id.result5);
            title[5]= itemView.findViewById(R.id.title6);
            result[5] = itemView.findViewById(R.id.result6);

        }
    }
}


