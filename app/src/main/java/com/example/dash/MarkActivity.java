package com.example.dash;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MarkActivity extends AppCompatActivity {

    Spinner selected;
    RecyclerView recyclerView;
    String rollNo;
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    MarkAdapter markAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        Intent i = getIntent();
        rollNo = i.getStringExtra("rollNo");

        selected =  findViewById(R.id.select);
        recyclerView = findViewById(R.id.recyclerView);
        selected.setOnItemSelectedListener(new ItemSelected(rollNo));



        markAdapter = new MarkAdapter(dataList);
        recyclerView.setAdapter(markAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



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

            selected = "Marks/"+selected+"/"+rollNo;
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
