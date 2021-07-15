package com.example.elderapp.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.elderapp.R
import com.google.android.material.button.MaterialButtonToggleGroup

class RegisterActivity : AppCompatActivity() {
    var manager: FragmentManager? = supportFragmentManager
    var elderFragment: RegisterElderFragment? = RegisterElderFragment()
    var volunteerFragment: RegisterVolunteerFragment? = RegisterVolunteerFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //start with elder fragment
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_register, elderFragment)
        transaction.commit()
        val toggleGroup = findViewById<MaterialButtonToggleGroup?>(R.id.materialButtonToggleGroup)
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (checkedId == R.id.elderPage) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragment_register, elderFragment)
                transaction.commit()
            } else if (checkedId == R.id.volunteerPage) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragment_register, volunteerFragment)
                transaction.commit()
            }
        }
    }
}