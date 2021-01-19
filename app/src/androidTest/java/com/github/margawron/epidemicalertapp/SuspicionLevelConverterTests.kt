package com.github.margawron.epidemicalertapp

import com.github.margawron.epidemicalertapp.data.alerts.SuspicionLevel
import com.github.margawron.epidemicalertapp.data.alerts.SuspicionLevelConverter
import org.junit.Assert
import org.junit.Test

class SuspicionLevelConverterTests {

    private val converter = SuspicionLevelConverter()

    @Test
    fun shouldCorrectlyConvertEnums(){
        val dismissed = converter.fromEnum(SuspicionLevel.DISMISSED)
        val probable = converter.fromEnum(SuspicionLevel.PROBABLE)
        val sick = converter.fromEnum(SuspicionLevel.SICK)
        Assert.assertTrue(SuspicionLevel.DISMISSED == converter.fromString(dismissed))
        Assert.assertTrue(SuspicionLevel.PROBABLE == converter.fromString(probable))
        Assert.assertTrue(SuspicionLevel.SICK == converter.fromString(sick))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowException(){
        converter.fromString("OE")
    }
}