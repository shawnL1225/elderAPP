package com.example.elderapp.register;

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
import com.example.elderapp.LoginActivity;
import com.example.elderapp.R;

import java.util.HashMap;
import java.util.Map;


public class RegisterElderFragment extends Fragment {

    private EditText etName, etPhone, etPassword, etPasswordC, etRemarks1, etRemarks2,etRemarks3, etContactPhone, etContactEmail;
    private final String url = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/register.php";
    private String username, phone, pass, passC, remarks1 = "", remarks2 = "", remarks3 = "", contactPhone, contactEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register_elder, container, false);
        etName = root.findViewById(R.id.et_name);
        etPhone = root.findViewById(R.id.et_phone);
        etPassword = root.findViewById(R.id.et_password);
        etPasswordC = root.findViewById(R.id.et_passwordCheck);
        etRemarks1 = root.findViewById(R.id.et_remarks1);
        etRemarks2 = root.findViewById(R.id.et_remarks2);
        etRemarks3 = root.findViewById(R.id.et_remarks3);
        etContactPhone = root.findViewById(R.id.et_contactPhone);
        etContactEmail = root.findViewById(R.id.et_contactEmail);
        Button register = root.findViewById(R.id.btn_register);
        TextView toLogin = root.findViewById(R.id.tv_toLogin);

        register.setOnClickListener(view -> {
            username = etName.getText().toString().trim();
            phone = etPhone.getText().toString().trim();
            pass = etPassword.getText().toString().trim();
            passC = etPasswordC.getText().toString().trim();
            remarks1 = etRemarks1.getText().toString().trim();
            remarks2 = etRemarks2.getText().toString().trim();
            remarks3 = etRemarks3.getText().toString().trim();
            contactPhone = etContactPhone.getText().toString().trim();
            contactEmail = etContactEmail.getText().toString().trim();

            if(!pass.equals(passC)){
                Toast.makeText(getContext(), "密碼不相符", Toast.LENGTH_SHORT).show();
            }
            else if(!username.equals("") && !phone.equals("") && !contactPhone.equals("")){
                SQL();
            }
            
        });
        toLogin.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        return root;
    }

    public void SQL() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("connect", "Response: "+response);
            if (response.startsWith("success")) {
                Intent it = new Intent(getContext(), LoginActivity.class);
                it.putExtra("signUp", true);
                startActivity(it);
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
                data.put("remarks1", remarks1);
                data.put("remarks2", remarks2);
                data.put("remarks3", remarks3);
                data.put("contactPhone", contactPhone);
                data.put("contactEmail", contactEmail);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}
