package com.example.elderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{

    String account, password;
    EditText etAccount, etPassword;
    private String url = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
    }




    public void register(View view){
        Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(it);
        finish();
    }

    public void login(View view) {
        account = etAccount.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if(!account.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Log.d("connect", "Response: "+response);
                if (response.startsWith("success")) {
                    Intent it = new Intent(LoginActivity.this, ElderActivity.class);;
                    if(response.endsWith("1")){
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

        }else{
            Toast.makeText(this, "請輸入帳號", Toast.LENGTH_SHORT).show();
        }
    }
}