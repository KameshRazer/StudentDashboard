package com.example.dash;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;


public class home extends AppCompatActivity {

    Button btnmenu,changepass,ecamp,so;
    RelativeLayout maincontent;
    LinearLayout mainmenu;
    Animation fromtop,frombottom;
    ImageView userpicbig,marks,attendance,circularImage,ctt,resultImage;
    TextView name,rollno,nameHome,rollNoHome;
    String rollNo,userName,imageURL;
    SharedPreferences logInfo;
    DatabaseReference dbRef;
    String TAG ="Home";
    Boolean isLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Button
        btnmenu =  findViewById(R.id.btnmenu);
        changepass =  findViewById(R.id.changepass);

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
        ctt=findViewById(R.id.ctt);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Student");
        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
        userName =  logInfo.getString("name","Error");
        rollNo = logInfo.getString("RollNo","Error");
        imageURL = logInfo.getString("imageUrl","Error");
        isLogged = logInfo.getBoolean("isLogged",false);
        if(!isLogged)
            startActivity(new Intent(home.this,FirstActivity.class));


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

        ecamp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://ecampus.psgtech.ac.in/studzone2/"));
                startActivity(viewIntent);
            }
        });

        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText(userName);
                rollno.setText(rollNo);
                maincontent.animate().translationX(0);
                mainmenu.animate().translationX(0);

                changepass.startAnimation(frombottom);

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

        ctt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mark = new Intent(home.this,class_time_table.class);
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

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"ramukuttytheiva@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Password Change Request");
                intent.putExtra(Intent.EXTRA_TEXT,"Enter Old Password: \nEnter New Password:");
                intent.putExtra(Intent.EXTRA_CC,"ramukuttytheiva@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
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
