package com.github.margawron.epidemicalertapp

import com.github.margawron.epidemicalertapp.data.users.AccountState
import com.github.margawron.epidemicalertapp.data.users.AccountStateConverter
import org.junit.Assert
import org.junit.Test

class AccountStateConverterTests {


    private val converter = AccountStateConverter()

    @Test
    fun shouldCorrectlyConvertEnums(){
        val normal = converter.fromEnum(AccountState.NORMAL)
        val banned = converter.fromEnum(AccountState.BANNED)
        Assert.assertTrue(AccountState.NORMAL == converter.fromString(normal))
        Assert.assertTrue(AccountState.BANNED == converter.fromString(banned))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowException(){
        converter.fromString("OE")
    }
}