package com.example.dash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button loginButton;
    SharedPreferences logInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        loginButton = findViewById(R.id.Loginbtn);
        logInfo = getSharedPreferences("LogInfo",MODE_PRIVATE);

        if(logInfo.getBoolean("isLogged", false)){
         Intent home = new Intent(FirstActivity.this, home.class);
         startActivity(home);
//            System.out.println("Already Logged In");
        }
//        System.out.println(logInfo.contains("isLogged"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
    }
}
