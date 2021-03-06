package com.example.calculatorapp

import kotlin.math.pow

/**
 * Calculations contains the parsing and calculations that the calculator app uses
 *
 * @constructor Create empty Calculations
 */
class Calculations {
    //used for order of operations calculations
    private val exp = 0
    private val mulDiv = 1
    private val addSub = 2

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
        var number = ""

        for (i in input.indices) {
            val character = input[i]

            //if current character is a number or decimal, add it to the current number
            if (character.isDigit() || character == '.') {
                number += character

                //current character is an operator
            } else {
                if (number.isNotEmpty()) {
                    list.add(number.toFloat())
                    number = ""
                }

                //if current character is '-' and there is an operator or nothing else before it, the next number will be negative
                if (character == '-' && (i == 0 || !input[i - 1].isDigit()))
                    number += character

                    //treat character as normal operator and add it to the list
                else
                    list.add(character)
            }
        }

        //handle the end of the list
        if (number != "")
            list.add(number.toFloat())
        return list
    }

    /**
     * Do operation calculates any exponents, multiplication, and division in the list using recursion.
     * It will do the first operation in the list based on the order of operations, then add the rest of the
     * elements to the result list and call doOperation on the result list.
     *
     * Inspired by the algorithm for multiplication and division found in [https://www.youtube.com/watch?v=2hSHgungOKI]
     *
     * @param input the input list of numbers and operators
     * @param operation the current level of order of operations
     * @return the result list after the operations are calculated
     */
    private fun doOperation(input: MutableList<Any>, operation: Int): MutableList<Any> {
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


        val result = mutableListOf<Any>()
        var restart = false
        val size = input.size

        //loop through input, do first calculation, then make recursive call
        var i = 0
        while(i < size) {
            if (input[i] is Char && i < size && !restart) {
                val current = input[i]
                val num1 = input[i - 1] as Float
                val num2 = input[i + 1] as Float

                when (operation) {
                    //exponent
                    exp -> {
                        if (current == '^') {
                            result.add(num1.pow(num2))
                            restart = true
                            i++
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
                                restart = true
                                i++
                            }
                            '/' -> {
                                result.add(num1 / num2)
                                restart = true
                                i++
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
                                restart = true
                                i++
                            }
                            '-' -> {
                                result.add(num1 - num2)
                                restart = true
                                i++                            }
                        }
                    }
                }
            } else if (restart) {
                //if an operation has been done, add the rest of the input list to the result list,
                //skipping over the current operator and next number
                result.add(input[i])
            }

            i++
        }

        return doOperation(result, operation)
    }
}