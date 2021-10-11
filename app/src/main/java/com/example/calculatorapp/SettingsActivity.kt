package com.example.calculatorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

/**
 * Settings activity
 *
 * @constructor Create empty Settings activity
 */
class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "default"

    /**
     * On create
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppTheme()

        setContentView(R.layout.activity_settings)

        //set up on click listeners for theme buttons
        val defaultTheme = findViewById<Button>(R.id.default_theme)
        defaultTheme.setOnClickListener { changeTheme(defaultTheme.id) }

        val matrix = findViewById<Button>(R.id.matrix_theme)
        matrix.setOnClickListener { changeTheme(matrix.id) }
    }

    private fun setAppTheme() {
        sharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)

        when (sharedPreferences.getString(themeKey, "default")) {
            "default" -> {
                theme.applyStyle(R.style.defaultTheme, true)
            }
            "matrix" -> {
                theme.applyStyle(R.style.matrix, true)
            }
        }
    }

    /**
     * On create options menu
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * On options item selected
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_calculator -> {
                startCalculatorActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Start calculator activity
     *
     */
    private fun startCalculatorActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Change theme
     *
     * @param id
     */
    private fun changeTheme(id: Int) {
        when (id) {
            R.id.default_theme -> {
                sharedPreferences.edit().putString(themeKey, "default").apply()
            }
            R.id.matrix_theme -> {
                sharedPreferences.edit().putString(themeKey, "matrix").apply()
            }
        }

        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }
}