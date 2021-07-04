package com.example.elderapp.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.elderapp.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Button btn_SignUpGo1 = (Button)findViewById(R.id.btn_SignUpGo1);
        Button btn_SignUpBack1 = (Button)findViewById(R.id.btn_SignUpGo1);
        btn_SignUpGo1.setOnClickListener(this);
        btn_SignUpBack1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_SignUpGo1:
                Intent fragment_sign_up1 = new Intent(SignUpActivity.this, com.example.elderapp.signup.fragment_sign_up1.class);
                startActivity(fragment_sign_up1);
                break;
            case R.id.btn_SignUpBack1:
                Intent LoginActivity = new Intent(SignUpActivity.this, com.example.elderapp.signup.LoginActivity.class);
                startActivity(LoginActivity);
                break;
        }
    }
}