package com.example.dash;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class MarkActivity extends AppCompatActivity {

    Spinner selected;
    RecyclerView recyclerView;
    SharedPreferences logInfo;
//    String rollNo;
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    ArrayList<String> testList = new ArrayList<>();
    MarkAdapter markAdapter;
    ArrayAdapter testAdapter;
    DatabaseReference dbRef;
    Button btnmenu;
    String rollNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
        rollNo = logInfo.getString("RollNo","Error");

        selected =  findViewById(R.id.select);
        recyclerView = findViewById(R.id.recyclerView);
        selected.setOnItemSelectedListener(new ItemSelected(rollNo));

        testAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,testList);
        testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selected.setAdapter(testAdapter);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Marks/"+rollNo.substring(0,4));
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                testList.clear();
                testList.add("Select Test");
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(!ds.getKey().equals("Result"))
                    testList.add(ds.getKey());
                }
                testAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        markAdapter = new MarkAdapter(dataList);
        recyclerView.setAdapter(markAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Button
        btnmenu =  findViewById(R.id.btnmenu1);
//        rollNoHome = findViewById(R.id.rollnohome);

        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mark = new Intent(MarkActivity.this,home.class);
                mark.putExtra("rollNo",rollNo);
                startActivity(mark);
            }
        });





    }
    public class ItemSelected implements AdapterView.OnItemSelectedListener {

        DatabaseReference dbRef;
        String rollNo;
        ItemSelected(String rollNo){
            this.rollNo = rollNo;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getSelectedItem().toString();
            selected = "Marks/"+rollNo.substring(0,4)+"/"+selected+"/"+rollNo;
            //selected = "Marks/"+selected+"/"+rollNo;
//            System.out.println(selected);
            dataList.clear();
            dbRef = FirebaseDatabase.getInstance().getReference().child(selected);
            dbRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        DatabaseReference chilRef = dbRef.getRef().child(Objects.requireNonNull(ds.getKey()));
                        chilRef.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<String  > list = new ArrayList<>();
                                list.clear();
                                for(DataSnapshot ds  : dataSnapshot.getChildren()){
                                    String key = ds.getKey();
                                    String value = Objects.requireNonNull(ds.getValue()).toString();
                                    list.add(key);
                                    list.add(value);
                                }
                                dataList.add(list);
                                markAdapter.notifyDataSetChanged();
//                                System.out.println(dataList.get(0));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

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
