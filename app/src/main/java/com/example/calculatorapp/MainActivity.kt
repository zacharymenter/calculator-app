package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true
    private var canAddSubtract = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: android.view.View) {
        if (view is Button) {
            inputTV.append(view.text)
            canAddOperation = true
        }
    }

    fun decimalAction(view: android.view.View) {
        if (view is Button) {
            if (canAddDecimal) {
                inputTV.append(view.text)
                canAddDecimal = false
            }
        }
    }

    fun subtractAction(view: android.view.View) {
        if (view is Button) {
            if (canAddSubtract) {
                inputTV.append(view.text)
                canAddSubtract = false
            }
        }
    }

    fun operationAction(view: android.view.View) {
        if (view is Button && canAddOperation) {
            inputTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: android.view.View) {
        inputTV.text = ""
        resultTV.text = ""
    }

    fun backSpaceAction(view: android.view.View) {
        val length = inputTV.length()
        if (length > 0)
            inputTV.text = inputTV.text.subSequence(0, length - 1)

    }

    fun equalsAction(view: android.view.View) {
        resultTV.text = calculateResults()
    }



    //should all of this go in a separate class?
    //calculates the result and returns the result string
    private fun calculateResults(): String {
        val input = parse()
        if (input.isEmpty()) {
            return ""
        }

        val timesDivision = calculateTimesDivision(input)
        if (timesDivision.isEmpty()) {
            return ""
        }

        val result = calculateAdditionSubtraction(timesDivision)

        return result.toString()
    }

    private fun calculateAdditionSubtraction(timesDivision: MutableList<Any>): Float {
        var result = timesDivision[0] as Float

        for (i in timesDivision.indices) {
            if (timesDivision[i] is Char && i != timesDivision.lastIndex) {
                val operator = timesDivision[i]
                val nextDigit = timesDivision[i + 1] as Float
                if (operator == '+') {
                    result += nextDigit
                }
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun calculateTimesDivision(input: MutableList<Any>): MutableList<Any> {
        var resultList = input
        while (resultList.contains('x') || resultList.contains('/')) {
            resultList = calcTimesDivHelper(resultList)
        }
        return resultList;
    }

    private fun calcTimesDivHelper(list: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()

        var restartIndex = list.size

        for (i in list.indices) {
            if (list[i] is Char && i != list.lastIndex && i < restartIndex) {
                val operator = list[i]
                val prevDigit = list[i - 1] as Float
                val nextDigit = list[i + 1] as Float

                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex) {
                newList.add(list[i])
            }
        }

        return newList
    }

    //parse the input string into a list of digits and operators
    private fun parse(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in inputTV.text) {
            if (character.isDigit() || character == '.') {
                currentDigit += character

                //allow for input of negative numbers
            } else if (character == '-') {
                list.add(currentDigit.toFloat())
                currentDigit = "" + character
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "") {
            list.add(currentDigit.toFloat())
        }

        return list
    }
}