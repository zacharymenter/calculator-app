package com.example.calculatorapp

import kotlin.math.pow

/**
 * Calculations contains the parsing and calculations that the calculator app uses
 *
 * @constructor Create empty Calculations
 */
class Calculations {
    //used for order of operations calculations
    private val exp = 'e'
    private val mulDiv = 'm'
    private val addSub = 'a'

    /**
     * Calculate parses the input, does the calculations based on the input, and returns the result
     *
     * @param input the input string from the main activity
     * @return the result of the calculation
     */
    fun calculate(input: CharSequence): Float {
        val parsed = parse(input)

        //start with exponents first
        val result = doOperation(parsed, exp)

        //if calculated properly, resulting list should only contain 1 element
        return if (result.size == 1)
            result[0] as Float
        else
            error("Resulting array had more than 1 element")
    }

    /**
     * Parse parses the input string into a list of digits and operators
     *
     * @param input the input string
     * @return the resulting list of digits and operators
     */
    private fun parse(input: CharSequence): MutableList<Any> {
        val list = mutableListOf<Any>()
        var digit = ""

        for (i in input.indices) {
            val character = input[i]

            //if current character is a digit or decimal, add it to the current digit
            if (character.isDigit() || character == '.') {
                digit += character

                //current character is an operator
            } else {
                if (digit.isNotEmpty()) {
                    list.add(digit.toFloat())
                    digit = ""
                }

                //if current character is '-' and there is an operator or nothing else before it, the next digit will be negative
                if (character == '-' && (i == 0 || !input[i - 1].isDigit()))
                    digit += character

                    //treat character as normal operator and add it to the list
                else
                    list.add(character)
            }
        }

        //handle the end of the list
        if (digit != "")
            list.add(digit.toFloat())
        return list
    }

    /**
     * Do operation calculates any exponents, multiplication, and division in the list using recursion.
     * It will do the first operation in the list based on the order of operations, then add the rest of the
     * elements to the result list and call doOperation on the result list.
     *
     * Inspired by the algorithm for multiplication and division found in [https://www.youtube.com/watch?v=2hSHgungOKI]
     *
     * @param input
     * @param operation
     * @return
     */
    private fun doOperation(input: MutableList<Any>, operation: Char): MutableList<Any> {
        if (operation == exp && !input.contains('^')) {
            //no more '^' so, move on to 'x' and '/'
            return doOperation(input, mulDiv)
        }
        if (operation == mulDiv && !input.contains('x') && !input.contains('/')) {
            //no more 'x' and '/', so move on to '+' and '-'
            return doOperation(input, addSub)
        }

        //base case
        if (operation == addSub && !input.contains('+') && !input.contains('-'))
            //no more operations to be done, return result
            return input


        //do the first calculation and make the recursive call to do the rest
        val result = mutableListOf<Any>()
        var restart = input.size

        for (i in input.indices) {
            if (input[i] is Char && i < input.lastIndex && i < restart) {
                val current = input[i]
                val num1 = input[i - 1] as Float
                val num2 = input[i + 1] as Float

                when (operation) {
                    //exponent
                    exp -> {
                        if (current == '^') {
                            result.add(num1.pow(num2))
                            restart = i + 1
                        } else {
                            result.add(num1)
                            result.add(current)
                        }
                    }

                    //multiplication or division
                    mulDiv -> {
                        when (current) {
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
                                result.add(current)
                            }
                        }
                    }

                    //addition or subtraction
                    addSub -> {
                        when (current) {
                            '+' -> {
                                result.add(num1 + num2)
                                restart = i + 1
                            }
                            '-' -> {
                                result.add(num1 - num2)
                                restart = i + 1
                            }
                        }
                    }
                }
            }

            //if a calculation has been done, add the rest of the elements to result and make recursive call
            if (i > restart)
                result.add(input[i])
        }

        return doOperation(result, operation)
    }
}