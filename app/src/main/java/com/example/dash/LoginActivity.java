package com.example.dash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference dbRef;
    String rollNo;
    EditText userId,userPassword;
    SharedPreferences logInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userId = (EditText) findViewById(R.id.editText);
        userPassword = (EditText) findViewById(R.id.editText2);
        Button login = (Button) findViewById(R.id.button);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollNo = userId.getText().toString();
                dbRef = FirebaseDatabase.getInstance().getReference().child("Student/"+rollNo.substring(0,4));
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(rollNo))
                        {

                            String password  = dataSnapshot.child(rollNo+"/password").getValue(String.class);
                            String name = dataSnapshot.child(rollNo+"/name").getValue(String.class);
                            String imageUrl= dataSnapshot.child(rollNo+"/imageLink").getValue(String.class);
                            assert password != null;
                            if(password.equals(userPassword.getText().toString())) {

                                logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);
                                logInfo.edit().putString("RollNo",rollNo).apply();
                                logInfo.edit().putBoolean("isLogged", true).apply();
                                logInfo.edit().putString("name",name).apply();
                                logInfo.edit().putString("imageUrl",imageUrl).apply();

                                Intent home = new Intent(LoginActivity.this, home.class);
                                startActivity(home);
                            }
                                //Toast.makeText(getApplicationContext(),"Welcome "+name,Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                                userPassword.setText("");
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid User Id ",Toast.LENGTH_SHORT).show();
                            userId.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.d("DataBase Error ",databaseError.toException().toString());
                    }

                });
//                Toast.makeText(getApplicationContext(),"Data "+dbUser,Toast.LENGTH_SHORT).show();
            }
        });

    }
}

