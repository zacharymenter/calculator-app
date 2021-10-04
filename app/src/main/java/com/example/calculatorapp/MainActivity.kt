package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        backButton.setOnClickListener { backSpace() }

        val divButton = findViewById<Button>(R.id.divideButton)
        divButton.setOnClickListener{ addOperator(divButton.text.toString()) }

        val timesButton = findViewById<Button>(R.id.timesButton)
        timesButton.setOnClickListener{ addOperator(timesButton.text.toString()) }

        val plusButton = findViewById<Button>(R.id.plusButton)
        plusButton.setOnClickListener { addOperator(plusButton.text.toString()) }

        val minusButton = findViewById<Button>(R.id.minusButton)
        minusButton.setOnClickListener{ addMinus(minusButton.text.toString()) }

        val decButton = findViewById<Button>(R.id.decimalButton)
        decButton.setOnClickListener { addDecimal(decButton.text.toString()) }

        val equalsButton = findViewById<Button>(R.id.equalsButton)
        equalsButton.setOnClickListener { calculate() }
    }

    /**
     * Adds the pressed number text to the input text view
     */
    private fun addNumber(text: String) {
        inputText.append(text)
        canAddOperation = true
        canAddMinus = true
    }

    /**
     * Adds a decimal point to the input text view if allowed
     */
    private fun addDecimal(text: String) {
        if (canAddDecimal) {
            inputText.append(text)
            canAddDecimal = false
            canAddOperation = false
            canAddMinus = false
        }
    }

    /**
     * Adds a subtract symbol to the text view if allowed.
     */
    private fun addMinus(text: String) {
        if (canAddMinus) {
            inputText.append(text)
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
        if (length > 0) {
            inputText.text = inputText.text.subSequence(0, length - 1)

            //if the last character is a number or decimal, allow decimals and all operations
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
        }
    }

    /**
     * Calculates the result of the input
     */
    private fun calculate() {
        //only do calculations if input ends with a digit
        if (inputText.text[inputText.length() - 1].isDigit()) {
            val input = parse()
            val timesDiv = calculateTimesDivision(input)
            val result = calculateAddition(timesDiv)

            resultText.text = result.toString()
        }
    }

    /**
     * Parses the input string into a list of digits and operators
     */
    private fun parse(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var current = ""

        for (character in inputText.text) {
            if (character == '-') {
                //start a new digit
                if (current != "") {
                    list.add(current.toFloat())
                }
                current = ""
                current += character

            } else if (character.isDigit() || character == '.') {
                current += character

            //character must be some operator that isn't minus. Add current to list then add the operator
            } else {
                list.add(current.toFloat())
                current = ""
                list.add(character)
            }
        }

        //handle the end of the list
        if (current != "") {
            println(current.toFloat())
            list.add(current.toFloat())
        }

        return list
    }

    /**
     * Calculates any multiplication and division in the list using recursion
     */
    private fun calculateTimesDivision(input: MutableList<Any>): MutableList<Any> {
        //no more multiplication or division to be done
        if (!input.contains('x') && !input.contains('/')) {
            return input

        //do the first multiplication or division operation and make recursive call
        } else {
            val result = mutableListOf<Any>()

            var restart = input.size

            for (i in input.indices) {
                if (input[i] is Char && i < input.lastIndex && i < restart) {
                    val operator = input[i]
                    val prev = input[i - 1] as Float
                    val next = input[i + 1] as Float

                    when (operator) {
                        'x' -> {
                            result.add(prev * next)
                            restart = i + 1
                        }
                        '/' -> {
                            result.add(prev / next)
                            restart = i + 1
                        }
                        else -> {
                            result.add(prev)
                            result.add(operator)
                        }
                    }
                }

                if (i > restart) {
                    result.add(input[i])
                }
            }

            return calculateTimesDivision(result)
        }
    }

    /**
     * Calculates any addition (or subtraction through addition with negatives) in the
     * list returned from the times and division calculations
     */
    private fun calculateAddition(timesDiv: MutableList<Any>): Float {
        var result = timesDiv[0] as Float

        for (i in timesDiv.indices) {
            val current = timesDiv[i]
            if (current is Char && current == '+' && i < timesDiv.lastIndex) {
                result += timesDiv[i + 1] as Float

            //handles calculations for a number next to a negative number (e.g. 5-3)
            } else if (i > 0 && timesDiv[i - 1] is Float && current is Float && current < 0) {
                result += current
            }
        }

        return result
    }

}