package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true
    private var canAddMinus = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val acButton = findViewById<Button>(R.id.acButton)
        acButton.setOnClickListener{ allClear() }

        val backButton = findViewById<Button>(R.id.backspaceButton)
        backButton.setOnClickListener{ backSpace() }

        val expButton = findViewById<Button>(R.id.exponentButton)
        expButton.setOnClickListener{ addOperator("^") }

        val divButton = findViewById<Button>(R.id.divideButton)
        divButton.setOnClickListener{ addOperator("/") }

        val timesButton = findViewById<Button>(R.id.timesButton)
        timesButton.setOnClickListener{ addOperator("x") }

        val plusButton = findViewById<Button>(R.id.plusButton)
        plusButton.setOnClickListener { addOperator("+") }

        val minusButton = findViewById<Button>(R.id.minusButton)
        minusButton.setOnClickListener{ addMinus() }

        val decButton = findViewById<Button>(R.id.decimalButton)
        decButton.setOnClickListener { addDecimal() }

        val equalsButton = findViewById<Button>(R.id.equalsButton)
        equalsButton.setOnClickListener { calculate() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
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