package com.example.elderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elderapp.elder.ElderActivity;
import com.example.elderapp.register.RegisterActivity;
import com.example.elderapp.volunteer.VolunteerActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{

    String account, password;
    EditText etAccount, etPassword;
    String url = Global.url+"login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etAccount = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
    }




    public void toRegister(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void login(View view) {
        account = etAccount.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if(account.equals("")){
            Toast.makeText(this, "請輸入帳號", Toast.LENGTH_SHORT).show();

        }else{
            SQL();
        }
    }


    private void SQL(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("connect", "Response: "+response);

            if (response.startsWith("success")) {
                // formation would be "success-userID-userIdentity"
                String[] sep = response.split("-");
                getSharedPreferences("mySP", MODE_PRIVATE)
                        .edit().putString("uid", sep[1]).apply();

                Intent it = new Intent(LoginActivity.this, ElderActivity.class);;
                if(sep[2].equals("1")){
                    it = new Intent(LoginActivity.this, VolunteerActivity.class);;
                }


                startActivity(it);
//                    finish();
            } else if (response.equals("NoUser")) {
                Toast.makeText(LoginActivity.this, "登入失敗 - 查無此使用者", Toast.LENGTH_SHORT).show();
            }else if (response.equals("WrongPass")) {
                Toast.makeText(LoginActivity.this, "登入失敗 - 密碼錯誤", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(LoginActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("phone", account);
                data.put("password", password);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}