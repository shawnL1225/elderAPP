package com.example.elderapp.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.elderapp.R;
import com.example.elderapp.VolunteerActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn_login = (Button)findViewById(R.id.btn_login);
        Button btn_regist = (Button)findViewById(R.id.btn_regist);
        btn_login.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                Intent it_volunteer = new Intent(LoginActivity.this, VolunteerActivity.class);
                startActivity(it_volunteer);
                break;
            case R.id.btn_regist:
                Intent it_signup = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(it_signup);
                break;
        }
    }
}