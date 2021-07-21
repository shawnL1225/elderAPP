package com.example.elderapp.register

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.elderapp.R
import com.google.android.material.button.MaterialButtonToggleGroup


class RegisterActivity : AppCompatActivity() {
    var manager: FragmentManager = supportFragmentManager
    var elderFragment: RegisterElderFragment = RegisterElderFragment()
    var volunteerFragment: RegisterVolunteerFragment = RegisterVolunteerFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //start with elder fragment
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_register, elderFragment)
        transaction.commit()
        val btnElder = findViewById<Button>(R.id.elderPage)
        val btnVolunteer = findViewById<Button>(R.id.volunteerPage)
        val toggleGroup = findViewById<MaterialButtonToggleGroup?>(R.id.materialButtonToggleGroup)
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(isChecked){
                if (checkedId == R.id.elderPage) {
                    Log.d("button", "elder")
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_register, elderFragment)
                    transaction.commit()
                    btnElder.textSize = 20.0F
                    btnVolunteer.textSize = 14.0F
                } else if (checkedId == R.id.volunteerPage) {
                    Log.d("button", "volun")
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_register, volunteerFragment)
                    transaction.commit()
                    btnVolunteer.textSize = 20.0F
                    btnElder.textSize = 14.0F
                }
            }

        }
    }
}