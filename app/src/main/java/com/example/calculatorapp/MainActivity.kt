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

/**
 * Main activity is the calculator activity that allows the user to enter input and
 * calculate the result
 *
 * @constructor Create empty Main activity
 */
class MainActivity : AppCompatActivity() {
    //for saving data between layouts
    private val inputKey = "INPUT_KEY"
    private val resultKey = "RESULT_KEY"

    //for input validation
    private var canAddOperation = false
    private var canAddDecimal = true
    private var canAddMinus = true

    //for setting the theme of the app
    private lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "default"

    /**
     * On create creates the main activity
     *
     * @param savedInstanceState state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppTheme()

        setContentView(R.layout.activity_main)

        //set up on click listeners for all buttons
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

    /**
     * Set app theme sets the color theme based on the selected theme in the settings activity
     */
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
     * On save instance state saves the state of the activity on device rotation
     *
     * @param outState the state to be saved
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(inputKey, inputText.text.toString())
        outState.putString(resultKey, resultText.text.toString())
    }

    /**
     * On restore instance state restores the state of the activity when created
     *
     * @param savedInstanceState the state to restore from
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText.text = savedInstanceState.getString(inputKey)
        resultText.text = savedInstanceState.getString(resultKey)
    }

    /**
     * Start settings activity launches the settings activity
     */
    private fun startSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    /**
     * On create options menu creates the hamburger menu for the app
     *
     * @param menu the menu of the app
     * @return a success boolean
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * On options item selected starts the activity based on the option that was selected
     * in the menu
     *
     * @param item the menu item that was selected
     * @return a success boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                startSettingsActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Add number adds the pressed number text to the input text view
     *
     * @param text the text of the number that was pressed
     */
    private fun addNumber(text: String) {
        resultText.text = ""
        inputText.append(text)
        canAddOperation = true
        canAddMinus = true
    }

    /**
     * Add decimal adds a decimal point to the input text view if allowed
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
     * Add minus adds a subtract symbol to the text view if allowed
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
     * Add operator adds an operation symbol to the text view
     *
     * @param text
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
     * All clear clears the input and result text views and resets input boolean values
     */
    private fun allClear() {
        inputText.text = ""
        resultText.text = ""
        canAddDecimal = true
        canAddMinus = true
        canAddOperation = false
    }

    /**
     * Backspace removes the most recent character in the input text view and sets boolean values for
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
     * Calculate calculates the result of the input
     */
    private fun calculate() {
        //only do calculations if input ends with a digit
        if (inputText.text[inputText.length() - 1].isDigit())
            resultText.text = Calculations().calculate(inputText.text).toString()
    }
}