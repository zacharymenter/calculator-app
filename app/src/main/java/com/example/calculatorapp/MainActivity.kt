package com.example.calculatorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    private val inputKey = "INPUT_KEY"
    private val resultKey = "RESULT_KEY"

    private var canAddOperation = false
    private var canAddDecimal = true
    private var canAddMinus = true

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

        setContentView(R.layout.activity_main)

        val button0 = findViewById<Button>(R.id.button0)
        button0.setOnClickListener{ addNumber(button0.text.toString()) }

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener{ addNumber(button1.text.toString()) }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener{ addNumber(button2.text.toString()) }

        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener{ addNumber(button3.text.toString()) }

        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener{ addNumber(button4.text.toString()) }

        val button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener{ addNumber(button5.text.toString()) }

        val button6 = findViewById<Button>(R.id.button6)
        button6.setOnClickListener{ addNumber(button6.text.toString()) }

        val button7 = findViewById<Button>(R.id.button7)
        button7.setOnClickListener{ addNumber(button7.text.toString()) }

        val button8 = findViewById<Button>(R.id.button8)
        button8.setOnClickListener{ addNumber(button8.text.toString()) }

        val button9 = findViewById<Button>(R.id.button9)
        button9.setOnClickListener{ addNumber(button9.text.toString()) }

        findViewById<Button>(R.id.acButton).setOnClickListener{ allClear() }

        findViewById<Button>(R.id.backspaceButton).setOnClickListener{ backSpace() }

        findViewById<Button>(R.id.exponentButton).setOnClickListener{ addOperator("^") }

        findViewById<Button>(R.id.divideButton).setOnClickListener{ addOperator("/") }

        findViewById<Button>(R.id.timesButton).setOnClickListener{ addOperator("x") }

        findViewById<Button>(R.id.plusButton).setOnClickListener { addOperator("+") }

        findViewById<Button>(R.id.minusButton).setOnClickListener{ addMinus() }

        findViewById<Button>(R.id.decimalButton).setOnClickListener { addDecimal() }

        findViewById<Button>(R.id.equalsButton).setOnClickListener { calculate() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(inputKey, inputText.text.toString())
        outState.putString(resultKey, resultText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText.text = savedInstanceState.getString(inputKey)
        resultText.text = savedInstanceState.getString(resultKey)
    }

    private fun startSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                startSettingsActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Adds the pressed number text to the input text view
     */
    private fun addNumber(text: String) {
        resultText.text = ""
        inputText.append(text)
        canAddOperation = true
        canAddMinus = true
    }

    /**
     * Adds a decimal point to the input text view if allowed
     */
    private fun addDecimal() {
        if (canAddDecimal) {
            resultText.text = ""
            inputText.append(".")
            canAddDecimal = false
            canAddOperation = false
            canAddMinus = false
        }
    }

    /**
     * Adds a subtract symbol to the text view if allowed.
     */
    private fun addMinus() {
        if (canAddMinus) {
            resultText.text = ""
            inputText.append("-")
            canAddMinus = false

            //if a subtraction symbol is added, you cannot have another operation immediately follow it
            //but you could have a decimal point
            canAddOperation = false
            canAddDecimal = true
        }
    }

    /**
     * Adds an operation symbol to the text view
     */
    private fun addOperator(text: String) {
        if (canAddOperation) {
            resultText.text = ""
            inputText.append(text)
            canAddOperation = false

            //if an operation has been added, a decimal can follow
            canAddDecimal = true
        }
    }

    /**
     * Clears the input and result text views and resets input boolean values
     */
    private fun allClear() {
        inputText.text = ""
        resultText.text = ""
        canAddDecimal = true
        canAddMinus = true
        canAddOperation = false
    }

    /**
     * Removes the most recent character in the input text view and sets boolean values for
     * what can be entered after backspacing
     */
    private fun backSpace() {
        val length = inputText.length()
        resultText.text = ""

        //if the last character is a number or decimal, allow decimals and all operations
        if (length > 1) {
            inputText.text = inputText.text.subSequence(0, length - 1)
            val end = inputText.text[inputText.length() - 1]
            if (end.isDigit() || end == '.') {
                canAddDecimal = true
                canAddOperation = true
                canAddMinus = true
            } else {
                canAddOperation = false
            }

            //if there is a decimal anywhere between the end of the input string and the last operator, don't allow decimals
            for (i in inputText.length() - 1 downTo 0) {
                val current = inputText.text[i]
                if (!current.isDigit()) {
                    if (current == '.') {
                        canAddDecimal = false
                    }
                    break
                }
            }
        } else {
            allClear()
        }
    }

    /**
     * Calculates the result of the input
     */
    private fun calculate() {
        //only do calculations if input ends with a digit
        if (inputText.text[inputText.length() - 1].isDigit())
            resultText.text = Calculations().calculate(inputText.text).toString()
    }
}