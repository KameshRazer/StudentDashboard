package com.example.dash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class home extends AppCompatActivity {

    Button btnmenu,circular,timetable,ecamp,so;
    RelativeLayout maincontent;
    LinearLayout mainmenu;
    Animation fromtop,frombottom;
    ImageView userpicbig,marks;
    TextView name,rollno,nameHome,rollNoHome;
    String rollNo,userName,imageURL;
    SharedPreferences logInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Button
        btnmenu =  findViewById(R.id.btnmenu);
        circular =  findViewById(R.id.circular);
        timetable =  findViewById(R.id.timetable);
        ecamp =  findViewById(R.id.ecamp);
        so =  findViewById(R.id.so);

        //TextView
        name = findViewById(R.id.name);
        rollno = findViewById(R.id.rollno);
        nameHome = findViewById(R.id.userhome);
        rollNoHome = findViewById(R.id.rollnohome);

        //ImageView
        userpicbig = findViewById(R.id.userpicbig);
        marks = findViewById(R.id.marks);


        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
        userName =  logInfo.getString("name","Error");
        rollNo = logInfo.getString("RollNo","Error");
        imageURL = logInfo.getString("imageUrl","Error");

//        dbRef = FirebaseDatabase.getInstance().getReference().child(rollNo);

        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        //Layout
        maincontent =  findViewById(R.id.maincontent);
        mainmenu =  findViewById(R.id.mainmenu);

        nameHome.setText(userName);
        rollNoHome.setText(rollNo);

        Picasso.get().load(imageURL).resize(400,350).into(userpicbig);
//        Picasso.with(home.this).load(imageURL)
//                .resize(400,350)
//                .into(userpicbig);

        so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
                logInfo.edit().clear().apply();
                startActivity(new Intent(home.this,LoginActivity.class));
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

            }
        });
        maincontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maincontent.animate().translationX(-735);
                mainmenu.animate().translationX(-735);
            }
        });

        marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mark = new Intent(home.this,MarkActivity.class);
                mark.putExtra("rollNo",rollNo);
                startActivity(mark);
            }
        });
    }

}
