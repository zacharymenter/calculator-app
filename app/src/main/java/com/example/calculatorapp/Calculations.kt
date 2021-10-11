package com.example.calculatorapp

import kotlin.math.pow

/**
 * Calculations
 *
 * @constructor Create empty Calculations
 */
class Calculations {
    private val exp = 'e'
    private val mulDiv = 'm'
    private val addSub = 'a'

    /**
     * Calculate
     *
     * @param input
     * @return
     */
    fun calculate(input: CharSequence): Float {
        val parsed = parse(input)
        val result = doOperation(parsed, exp)

        return if (result.size == 1)
            result[0] as Float
        else
            error("Resulting array had more than 1 element")
    }

    /**
     * Parse arses the input string into a list of digits and operators
     *
     * @param input
     * @return
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
                if (character == '-' && (i == 0 || !input[i - 1].isDigit()))
                    digit += character

                    //treat as normal operator and add it to the list
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
     * Do operation calculates any exponents, multiplication, and division in the list using recursion
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

            if (i > restart)
                result.add(input[i])
        }

        return doOperation(result, operation)
    }
}