package com.example.dash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;


public class home extends AppCompatActivity {

    Button btnmenu,circular,timetable,ecamp,so;
    RelativeLayout maincontent;
    LinearLayout mainmenu;
    Animation fromtop,frombottom;
    ImageView userpicbig,marks,attendance,circularImage,resultImage;
    TextView name,rollno,nameHome,rollNoHome;
    String rollNo,userName,imageURL;
    SharedPreferences logInfo;
    DatabaseReference dbRef;
    String TAG ="Home";
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
        attendance = findViewById(R.id.attendance);
        circularImage = findViewById(R.id.circularImage);
        resultImage = findViewById(R.id.result_image);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Student");
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
                dbRef.child(rollNo.substring(0,4)+"/"+rollNo+"/TokenId").setValue(null);
                logInfo.edit().clear().apply();
                startActivity(new Intent(home.this,FirstActivity.class));
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
                startActivity(mark);
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,AttendanceActivity.class));
            }
        });

        circularImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,CircularActivity.class));
            }
        });

        circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,CircularActivity.class));
            }
        });
        resultImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this,TotalMarkActivity.class));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId  = getResources().getString(R.string.channel_id);
            String channelName = getResources().getString(R.string.app_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        dbRef.child(rollNo.substring(0,4)+"/"+rollNo+"/TokenId").setValue(token);
                    }
                });
    }

}
