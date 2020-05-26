package com.example.dash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TotalMarkActivity extends AppCompatActivity {

    SharedPreferences logInfo;
    RecyclerView recyclerView;
    DatabaseReference dbRef;
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    String rollNo;
    TotalMarkAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_mark);

        recyclerView = findViewById(R.id.atd_recyclerView);
        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
        rollNo = logInfo.getString("RollNo","Error");

        dataAdapter = new TotalMarkAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!rollNo.equals("Error")){
            dbRef = FirebaseDatabase.getInstance().getReference().child("Attendance/"+rollNo.substring(0,4)+"/"+rollNo);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        ArrayList<String> list = new ArrayList<>();
                        list.add(ds.getKey());
                        list.add(ds.child("Total Hours").getValue().toString());
                        list.add(ds.child("Present").getValue().toString());
                        dataList.add(list);
                    }
                    dataAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{

        }
    }
}
