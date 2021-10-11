package com.example.calculatorapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Unit tests for the [Calculations] class
 */
class CalculationsTests {
    private val calculations = Calculations()

    /**
     * Test_add tests a single add operation
     */
    @Test
    fun test_add() {
        val result = calculations.calculate("5+5")
        assertEquals(10F, result)
    }

    /**
     * Test_multiple_add tests multiple add operations
     */
    @Test
    fun test_multiple_add() {
        val result = calculations.calculate("1+2+3+4+5")
        assertEquals(15F, result)
    }

    /**
     * Test_subtract tests a single subtract operation
     */
    @Test
    fun test_subtract() {
        val result = calculations.calculate("10-5")
        assertEquals(5F, result)
    }

    /**
     * Test_multiple_subtract tests multiple subtract operations
     */
    @Test
    fun test_multiple_subtract() {
        val result = calculations.calculate("50-5-4-3-2-1")
        assertEquals(35F, result)
    }

    /**
     * Test_add_subtract tests addition and subtraction together
     */
    @Test
    fun test_add_subtract() {
        val result = calculations.calculate("50-20+5-15")
        assertEquals(20F, result)
    }

    /**
     * Test_negative_numbers tests input with negative numbers
     */
    @Test
    fun test_negative_numbers() {
        val result = calculations.calculate("-100+-50+35-25")
        assertEquals(-140F, result)
    }

    /**
     * Test_multiply tests a single multiplication operation
     */
    @Test
    fun test_multiply() {
        val result = calculations.calculate("5x5")
        assertEquals(25F, result)
    }

    /**
     * Test_multiple_multiply tests multiple multiplication operations
     */
    @Test
    fun test_multiple_multiply() {
        val result = calculations.calculate("5x5x10x2")
        assertEquals(500F, result)
    }

    /**
     * Test_division tests a single division operation
     */
    @Test
    fun test_division() {
        val result = calculations.calculate("15/3")
        assertEquals(5F, result)
    }

    /**
     * Test_multiple_division tests multiple division operations
     */
    @Test
    fun test_multiple_division() {
        val result = calculations.calculate("30/3/5/2")
        assertEquals(1F, result)
    }

    /**
     * Test_multiply_divide tests multiplication and division together
     */
    @Test
    fun test_multiply_divide() {
        val result = calculations.calculate("30/3x2x3/6x5")
        assertEquals(50F, result)
    }

    /**
     * Test_decimal tests a single operation with decimals
     */
    @Test
    fun test_decimal() {
        val result = calculations.calculate(".5+.75")
        assertEquals(1.25F, result)
    }

    /**
     * Test_multiple_decimal tests multiple operations with decimals
     */
    @Test
    fun test_multiple_decimal() {
        val result = calculations.calculate(".5+.5+1+.75+.75")
        assertEquals(3.5F, result)
    }

    /**
     * Test_exponent tests a single exponent operation
     */
    @Test
    fun test_exponent() {
        val result = calculations.calculate("2^2")
        assertEquals(4F, result)
    }

    /**
     * Test_multiple_exponent tests multiple exponent operations
     */
    @Test
    fun test_multiple_exponent() {
        val result = calculations.calculate("2^2^3^2")
        assertEquals(4096F, result)
    }

    /**
     * Test_all_operations tests all operations together
     */
    @Test
    fun test_all_operations() {
        val result = calculations.calculate("2+2.5x5^2-3x2+-4.5/2^2/5")
        assertEquals(58.275F, result)
    }
}