package com.example.mybmi

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.example.mybmi.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val sharedPref = applicationContext.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val bmiJson = sharedPref.getString("BMI list", null)
        val editor = sharedPref.edit()
        val gson = Gson()

        var listOfBMI = mutableListOf<Float>()

        if (bmiJson == null) {
            listOfBMI.clear()
        } else {
            listOfBMI = gson.fromJson(bmiJson, Array<Float>::class.java).toMutableList()
        }

        val dateJson = sharedPref.getString("DateList", null)
        var listOfDate = mutableListOf<String>()
        if (dateJson == null) {
            listOfDate.clear()
        } else {
            listOfDate = gson.fromJson(dateJson, Array<String>::class.java).toMutableList()
        }
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val heightText = findViewById<TextView>(R.id.pituusTeksti)
        heightText.text = prefs.getString("pituus", "")

        val result = findViewById<TextView>(R.id.tulos)
        var calculation: Float
        var date: String

        findViewById<Button>(R.id.buttonLaske).setOnClickListener {

            val bmi = findViewById<TextView>(R.id.sinunBMI)
            val weightText = findViewById<EditText>(R.id.numeroPaino).text.toString()
            val heightBMI = heightText.text.toString()

            if (weightText != "" && heightBMI != "") {
                    val weightNumber = weightText.toDouble()
                    val heightNumberMeter = heightBMI.toDouble().div(100)
                    calculation = (weightNumber / heightNumberMeter.times(heightNumberMeter)).toFloat()
                    bmi.isVisible = true
                    result.isVisible = true
                    result.text = String.format("%.2f", calculation)
                    listOfBMI.add(calculation)
                    date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
                    listOfDate.add(date)
                    val json = gson.toJson(listOfBMI)
                    val datejson = gson.toJson(listOfDate)
                    editor.putString("BMI list", json)
                    editor.putString("DateList", datejson)
                    editor.apply()

            } else if (heightBMI == "") {
                val toast = Toast.makeText(this@MainActivity, getString(R.string.maarita_pituus), Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 500)
                toast.show()

            } else {
                val toast = Toast.makeText(this@MainActivity, getString(R.string.maarita_paino), Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 500)
                toast.show()
            }
        }
        findViewById<Button>(R.id.historia).setOnClickListener {
            val intent = Intent(this@MainActivity, BMIhistoria::class.java)
            startActivity(intent)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}



