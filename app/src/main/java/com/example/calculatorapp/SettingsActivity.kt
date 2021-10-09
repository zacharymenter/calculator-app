package com.example.calculatorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    val themeKey = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)

        when (sharedPreferences.getString(themeKey, "default")) {
            "default" -> {
                theme.applyStyle(R.style.defaultTheme, true)
            }
            "matrix" -> {
                theme.applyStyle(R.style.matrix, true)
            }
            "colorful" -> {
                theme.applyStyle(R.style.colorful, true)
            }
        }

        setContentView(R.layout.activity_settings)

        val defaultTheme = findViewById<Button>(R.id.default_theme)
        defaultTheme.setOnClickListener { changeTheme(defaultTheme.id) }

        val matrix = findViewById<Button>(R.id.matrix_theme)
        matrix.setOnClickListener { changeTheme(matrix.id) }

        val theme2 = findViewById<Button>(R.id.colorful_theme)
        theme2.setOnClickListener { changeTheme(theme2.id) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_calculator -> {
                startCalculatorActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startCalculatorActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun changeTheme(id: Int) {
        when (id) {
            R.id.default_theme -> {
                sharedPreferences.edit().putString(themeKey, "default").apply()
            }
            R.id.matrix_theme -> {
                sharedPreferences.edit().putString(themeKey, "matrix").apply()
            }
            R.id.colorful_theme -> {
                sharedPreferences.edit().putString(themeKey, "colorful").apply()
            }
        }

        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }
}