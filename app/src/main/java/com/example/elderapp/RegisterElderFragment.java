package com.example.elderapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;


public class RegisterElderFragment extends Fragment {

    private EditText etName, etPhone, etPassword, etPasswordC, etRemarks;
    private String url = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/register.php";
    private String username, phone, pass, passC, remarks = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register_elder, container, false);
        etName = root.findViewById(R.id.et_name);
        etPhone = root.findViewById(R.id.et_phone);
        etPassword = root.findViewById(R.id.et_password);
        etPasswordC = root.findViewById(R.id.et_passwordCheck);
        etRemarks = root.findViewById(R.id.et_remarks);
        Button register = root.findViewById(R.id.btn_register);
        TextView toLogin = root.findViewById(R.id.tv_toLogin);

        register.setOnClickListener(view -> {
            username = etName.getText().toString().trim();
            phone = etPhone.getText().toString().trim();
            pass = etPassword.getText().toString().trim();
            passC = etPasswordC.getText().toString().trim();
            remarks = etRemarks.getText().toString().trim();

            if(!pass.equals(passC)){
                Toast.makeText(getContext(), "密碼不相符", Toast.LENGTH_SHORT).show();
            }
            else if(!username.equals("") && !phone.equals("") && !pass.equals("")){
                SQL();
            }
        });
        toLogin.setOnClickListener(view -> {
            startActivity(new Intent(getContext(),LoginActivity.class));
        });

        return root;
    }

    public void SQL() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("connect", "Response: "+response);
            if (response.equals("success")) {
                Toast.makeText(getContext(), "已完成註冊", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
            else if (response.startsWith("failure")) {
                Toast.makeText(getContext(), "註冊失敗", Toast.LENGTH_SHORT).show();
            }

        }, error -> Toast.makeText(getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("username", username);
                data.put("phone", phone);
                data.put("password", pass);
                data.put("identity", "0");
                data.put("remarks", remarks);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}
