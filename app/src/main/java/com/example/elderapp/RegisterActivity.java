package com.example.elderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class RegisterActivity extends AppCompatActivity{

    FragmentManager manager = getSupportFragmentManager();
    RegisterElderFragment elderFragment = new RegisterElderFragment();
    RegisterVolunteerFragment volunteerFragment = new RegisterVolunteerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //start with elder fragment
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_register, elderFragment);
        transaction.commit();

        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.materialButtonToggleGroup);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if(checkedId == R.id.elderPage){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragment_register, elderFragment);
                    transaction.commit();
                }else if(checkedId == R.id.volunteerPage){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragment_register, volunteerFragment);
                    transaction.commit();
                }

            }
        });


    }





}