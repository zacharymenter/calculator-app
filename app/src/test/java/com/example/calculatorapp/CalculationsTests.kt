package com.example.calculatorapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Unit tests for the [Calculations] class
 */
class CalculationsTests {
    private val calculations = Calculations()

    @Test
    fun test_add() {
        val result = calculations.calculate("5+5")
        assertEquals(10F, result)
    }

    @Test
    fun test_multiple_add() {
        val result = calculations.calculate("1+2+3+4+5")
        assertEquals(15F, result)
    }

    @Test
    fun test_subtract() {
        val result = calculations.calculate("10-5")
        assertEquals(5F, result)
    }

    @Test
    fun test_multiple_subtract() {
        val result = calculations.calculate("50-5-4-3-2-1")
        assertEquals(35F, result)
    }

    @Test
    fun test_add_subtract() {
        val result = calculations.calculate("50-20+5-15")
        assertEquals(20F, result)
    }

    @Test
    fun test_negative_numbers() {
        val result = calculations.calculate("-100+-50+35-25")
        assertEquals(-140F, result)
    }
}