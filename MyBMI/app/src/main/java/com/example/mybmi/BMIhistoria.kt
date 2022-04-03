package com.example.mybmi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBar


class BMIhistoria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmihistoria)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPref = applicationContext.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        findViewById<Button>(R.id.poistaHistoria).setOnClickListener {
            editor.remove("BMI list")
            editor.remove("DateList")
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}