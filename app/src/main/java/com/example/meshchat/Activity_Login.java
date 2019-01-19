package com.example.meshchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editText_username = findViewById(R.id.edittext_username);
        Button button_signin = findViewById(R.id.button_signin);

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diveIn();
            }
        });
    }

    private void diveIn(){
        Intent i = new Intent(this, Activity_Main.class);
        startActivity(i);
        finish();
    }
}