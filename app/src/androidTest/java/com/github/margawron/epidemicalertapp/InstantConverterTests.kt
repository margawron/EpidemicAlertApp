package com.github.margawron.epidemicalertapp

import com.github.margawron.epidemicalertapp.data.measurments.InstantConverter
import org.junit.Assert
import org.junit.Test
import java.time.Instant

class InstantConverterTests {

    private val converter = InstantConverter()

    @Test
    fun shouldCorrectlyConvertInstant(){
        val instant = Instant.now()
        val milis = converter.instantToLong(instant)
        val shouldBeTheSameInstant = converter.longToInstant(milis)
        Assert.assertTrue(instant == shouldBeTheSameInstant)
    }

}