package com.example.calculatorapp

import kotlin.math.pow

class Calculations {

    fun calculate(input: CharSequence): Float {
        val parsed = parse(input)
        val exp = calculateExponents(parsed)
        val timesDiv = calculateTimesDivision(exp)
        return calculateAddSubtract(timesDiv)
    }

    /**
     * Parses the input string into a list of digits and operators
     */
    private fun parse(input: CharSequence): MutableList<Any> {
        val list = mutableListOf<Any>()
        var digit = ""

        for (i in input.indices) {
            val character = input[i]

            //if current character is a digit, add it to the current digit
            if (character.isDigit() || character == '.') {
                digit += character

                //current character is an operator
            } else {
                if (digit.isNotEmpty()) {
                    list.add(digit.toFloat())
                    digit = ""
                }

                //if current character is '-' and there is an operator or nothing else before it, the next digit will be negative
                if (character == '-' && (i == 0 || !input[i - 1].isDigit())) {
                    digit += character

                    //treat as normal operator and add it to the list
                } else {
                    list.add(character)
                }
            }
        }

        //handle the end of the list
        if (digit != "") {
            list.add(digit.toFloat())
        }
        return list
    }

    /**
     * Calculates any exponents in the list using recursion
     */
    //TODO() Integrate this with times and division since the logic is the same
    private fun calculateExponents(input: MutableList<Any>): MutableList<Any> {
        if (!input.contains('^')) {
            return input
        } else {
            val result = mutableListOf<Any>()

            var restart = input.size

            for (i in input.indices) {
                if (input[i] is Char && i < input.lastIndex && i < restart) {
                    val operator = input[i]
                    val num1 = input[i - 1] as Float
                    val num2 = input[i + 1] as Float
                    if (operator == '^') {
                        result.add(num1.pow(num2))
                        restart = i + 1
                    } else {
                        result.add(num1)
                        result.add(operator)
                    }
                }

                if (i > restart) {
                    result.add(input[i])
                }
            }

            return calculateExponents(result)
        }
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
                    val num1 = input[i - 1] as Float
                    val num2 = input[i + 1] as Float

                    when (operator) {
                        'x' -> {
                            result.add(num1 * num2)
                            restart = i + 1
                        }
                        '/' -> {
                            result.add(num1 / num2)
                            restart = i + 1
                        }
                        else -> {
                            result.add(num1)
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
     * Calculates any addition or subtraction in the list returned from the times and division calculations
     */
    private fun calculateAddSubtract(timesDiv: MutableList<Any>): Float {
        var result = timesDiv[0] as Float

        for (i in timesDiv.indices) {
            val current = timesDiv[i]
            if (current is Char && current == '+' && i < timesDiv.lastIndex) {
                result += timesDiv[i + 1] as Float
            } else if (current is Char && current == '-' && i < timesDiv.lastIndex) {
                result -= timesDiv[i + 1] as Float
            }
        }

        return result
    }
}