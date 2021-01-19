package com.github.margawron.epidemicalertapp

import com.github.margawron.epidemicalertapp.data.pathogens.PeriodResolution
import com.github.margawron.epidemicalertapp.data.pathogens.PeriodResolutionConverter
import com.github.margawron.epidemicalertapp.data.users.Role
import org.junit.Assert
import org.junit.Test


class PeriodResolutionConverterTests {

    private val converter = PeriodResolutionConverter()

    @Test
    fun shouldCorrectlyConvertEnums(){
        val days = converter.fromEnum(PeriodResolution.DAYS)
        val hours = converter.fromEnum(PeriodResolution.HOURS)
        val minutes = converter.fromEnum(PeriodResolution.MINUTES)
        Assert.assertTrue(PeriodResolution.DAYS == converter.fromString(days))
        Assert.assertTrue(PeriodResolution.HOURS == converter.fromString(hours))
        Assert.assertTrue(PeriodResolution.MINUTES == converter.fromString(minutes))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowException(){
        converter.fromString("OE")
    }
}