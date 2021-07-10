package com.example.elderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elderapp.adapter.Place;
import com.example.elderapp.adapter.PlaceAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPlaceActivity extends AppCompatActivity implements PlaceAdapter.ItemClickListener{

    private String url = Global.url+"setPlace.php";
    PlaceAdapter adapter;
    List<Place> placeList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);


        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        SQL_getPlaces();

    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = adapter.getTitle(position);
        int placeId = adapter.getId(position);
        builder.setTitle("刪除地點  -  "+title)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQL_deletePlace(placeId);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
//        Toast.makeText(this, "You clicked " + adapter.getTitle(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    private void SQL_deletePlace(int placeId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("connect", "delete Response: "+response);
            if(response.startsWith("success")){
                Global.putSnackBar(recyclerView, "成功刪除地點");
                SQL_getPlaces();
            }

        }, error -> {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("delete", "");
                data.put("id", String.valueOf(placeId));
                Log.d("connect", "placeid"+placeId);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void SQL_getPlaces(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("connect", "select Response: "+response);
            placeList.clear();
            try {
                JSONArray places = new JSONArray(response);
                for(int i=0; i<places.length(); i++){
                    JSONObject placeObj = places.getJSONObject(i);
                    String title = placeObj.getString("title");
                    String description = placeObj.getString("description");
                    int id = placeObj.getInt("id");


                    placeList.add(new Place(title, description, id));
                }

                adapter = new PlaceAdapter(this, placeList);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            }, error -> {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    String uid = getSharedPreferences("mySP", MODE_PRIVATE).getString("uid", "");
                    data.put("select", "");
                    data.put("uid", uid);
                    return data;
                }
            };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    EditText etPlaceTitle, etPlaceDesc;
    String insTitle, insDesc;
    public void addPlace(View view) {  //floating button
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_place, null);
        etPlaceDesc = dialogView.findViewById(R.id.et_placeDesc);
        etPlaceTitle = dialogView.findViewById(R.id.et_placeTitle);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView).setTitle("新增地點")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        insTitle = etPlaceTitle.getText().toString().trim();
                        insDesc = etPlaceDesc.getText().toString().trim();
                        if(!insTitle.equals("")){
                            SQL_storePlace();
                        }else {
                            Global.putSnackBarR(recyclerView, "請輸入地點名稱");
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void SQL_storePlace(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("connect", "store Response: "+response);
            if(response.startsWith("success")){
                SQL_getPlaces();
                Global.putSnackBar(recyclerView, "已設置地點");
            }else if(response.startsWith("failure")){
                Toast.makeText(EditPlaceActivity.this, "儲存失敗", Toast.LENGTH_SHORT).show();
            }


        }, error -> {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                String uid = getSharedPreferences("mySP", MODE_PRIVATE).getString("uid", "");
                data.put("insert", "");
                data.put("title", insTitle);
                data.put("desc", insDesc);
                data.put("uid", uid);
                return data;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

