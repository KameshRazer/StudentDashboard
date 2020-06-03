package com.example.dash;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class TotalMarkActivity extends AppCompatActivity {

    SharedPreferences logInfo;
    RecyclerView recyclerView;
    DatabaseReference dbRef;
    Spinner semester;
    ProgressDialog progressDialog;
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    ArrayList<String> semArray = new ArrayList<>();
    ArrayAdapter semAdapter;
    String rollNo;
    Button back4;
    TotalMarkAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_mark);

        recyclerView = findViewById(R.id.totalMarks_recyclerview);
        semester = findViewById(R.id.semester);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("LoAdInG...");
        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
        rollNo = logInfo.getString("RollNo","Error");

        back4 = findViewById(R.id.back4);
//        semArray.add("Select");
        dataAdapter = new TotalMarkAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        semAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,semArray);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(semAdapter);
        semester.setOnItemSelectedListener(new CreateSemResult());
        back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mark = new Intent(TotalMarkActivity.this,home.class);
                startActivity(mark);
            }
        });
        progressDialog.show();
        if(!rollNo.equals("Error")){
            dbRef = FirebaseDatabase.getInstance().getReference().child("Marks/"+rollNo.substring(0,4)+"/Result/"+rollNo);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    semArray.clear();
                    semArray.add("Select Semester");
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        semArray.add(ds.getKey());
                    }
                    semAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Roll Number Error",Toast.LENGTH_LONG).show();
        }
    }
    class CreateSemResult implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            progressDialog.show();
            String selected = parent.getSelectedItem().toString();
            dbRef = FirebaseDatabase.getInstance().getReference().child("Marks/"+rollNo.substring(0,4)+"/Result/"+rollNo+"/"+selected);
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataList.clear();
                    for(DataSnapshot code : dataSnapshot.getChildren()){
                        ArrayList<String> list = new ArrayList<>();
                        for(DataSnapshot ds : code.getChildren()){
                            list.add(Objects.requireNonNull(ds.getValue()).toString());
                        }
                        dataList.add(list);
//                        System.out.println("dataList : "+dataList);
                    }
                    dataAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
