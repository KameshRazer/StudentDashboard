package com.example.dash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TestTimeTable extends AppCompatActivity {

    SharedPreferences logInfo;
    ProgressDialog progressDialog;
    Spinner timeTable;
    ImageView img_timeTable;
    ArrayList<String> timeTableList = new ArrayList<>();
    ArrayAdapter timeTableAdapter;
    String rollNo;
    DatabaseReference dbRef;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_time_table);

        timeTable = findViewById(R.id.test_time_table);
        img_timeTable = findViewById(R.id.test_image);

        back= findViewById(R.id.back6);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mark = new Intent(TestTimeTable.this,home.class);
                startActivity(mark);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
        rollNo = logInfo.getString("RollNo","Error");

        timeTableAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,timeTableList);
        timeTableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeTable.setAdapter(timeTableAdapter);
        timeTable.setOnItemSelectedListener(new DisplayTimeTable());

        dbRef = FirebaseDatabase.getInstance().getReference().child("TestTimeTable/"+rollNo.substring(0,4));
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timeTableList.clear();
                timeTableList.add("Select Test");
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    timeTableList.add(ds.getKey());
                }
                timeTableAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    class DisplayTimeTable implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position>0) {
                progressDialog.show();
                final String selected = parent.getSelectedItem().toString();
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String timeTable = dataSnapshot.child(selected).getValue().toString();
                        Picasso.get().load(timeTable).resize(5000, 5000).into(img_timeTable);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
