package com.example.dash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CircularActivity extends AppCompatActivity {

    ArrayList<ArrayList<String >> dataList = new ArrayList<>();
    SharedPreferences logInfo;
    RecyclerView recyclerView;
    DatabaseReference dbRef;
    CircularAdaper circularAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);

        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
        String rollNo = logInfo.getString("RollNo","Error");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("LoAdInG...");

        recyclerView = findViewById(R.id.circularView);
        circularAdapter = new CircularAdaper(dataList);
        recyclerView.setAdapter(circularAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbRef = FirebaseDatabase.getInstance().getReference().child("Circular/"+rollNo.substring(0,4));
        progressDialog.show();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ArrayList<String> data = new ArrayList<>();
                    data.add(ds.getKey());
                    data.add(ds.child("message").getValue().toString());
                    dataList.add(data);
                }
                progressDialog.dismiss();
                circularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    class CircularAdaper extends RecyclerView.Adapter<CircularAdaper.ViewHolder>{

        private ArrayList<ArrayList<String >> dataList;
        CircularAdaper(ArrayList<ArrayList<String>> dataList){
            this.dataList = dataList;
        }
        @NonNull
        @Override
        public CircularAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context =parent.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View DataListView = layoutInflater.inflate(R.layout.display_circular,parent,false);
            ViewHolder viewHolder;
            viewHolder = new ViewHolder(DataListView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CircularAdaper.ViewHolder holder, int position) {
            ArrayList<String> data = dataList.get(position);
            holder.topic.setText(data.get(0));
            holder.message.setText(data.get(1));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView topic,message;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                topic = itemView.findViewById(R.id.topic);
                message = itemView.findViewById(R.id.circularMessage);
            }
        }
    }
}
