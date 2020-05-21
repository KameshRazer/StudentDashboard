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
//    String rollNo;
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    MarkAdapter markAdapter;

    Button btnmenu,circular,timetable,ecamp,so;
    RelativeLayout maincontent;
    LinearLayout mainmenu;
    Animation fromtop,frombottom;
    ImageView userpicbig;
    TextView name,rollno;
    String rollNo,userName,imageURL;
    SharedPreferences logInfo;

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

        //Button
        btnmenu =  findViewById(R.id.btnmenu1);
        circular =  findViewById(R.id.circular1);
        timetable =  findViewById(R.id.timetable1);
        ecamp =  findViewById(R.id.ecamp1);
        so =  findViewById(R.id.so1);

        //TextView
        name = findViewById(R.id.name1);
        rollno = findViewById(R.id.rollno1);
//        nameHome = findViewById(R.id.userhome);
//        rollNoHome = findViewById(R.id.rollnohome);

        //ImageView
        userpicbig = findViewById(R.id.userpicbig1);
//        marks = findViewById(R.id.marks);


        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
        userName =  logInfo.getString("name","Error");
        rollNo = logInfo.getString("RollNo","Error");
        imageURL = logInfo.getString("imageUrl","Error");

//        dbRef = FirebaseDatabase.getInstance().getReference().child(rollNo);

        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        //Layout
        maincontent =  findViewById(R.id.maincontent1);
        mainmenu =  findViewById(R.id.mainmenu1);

//        nameHome.setText(userName);
//        rollNoHome.setText(rollNo);

        Picasso.get().load(imageURL).resize(400,350).into(userpicbig);
//        Picasso.with(home.this).load(imageURL)
//                .resize(400,350)
//                .into(userpicbig);

        so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
                logInfo.edit().clear().apply();
                startActivity(new Intent(MarkActivity.this,LoginActivity.class));
            }
        });

        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText(userName);
                rollno.setText(rollNo);
                maincontent.animate().translationX(0);
                mainmenu.animate().translationX(0);

                circular.startAnimation(frombottom);
                timetable.startAnimation(frombottom);
                ecamp.startAnimation(frombottom);
                so.startAnimation(frombottom);

                name.startAnimation(fromtop);
                rollno.startAnimation(fromtop);
                userpicbig.startAnimation(fromtop);
//                System.out.println("Button clicked");

            }
        });
        maincontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maincontent.animate().translationX(-735);
                mainmenu.animate().translationX(-735);
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
