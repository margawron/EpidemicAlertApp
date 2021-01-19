package com.github.margawron.epidemicalertapp

import com.github.margawron.epidemicalertapp.data.users.Role
import com.github.margawron.epidemicalertapp.data.users.RoleConverter
import org.junit.Assert
import org.junit.Test


class RoleConverterTests {

    private val converter = RoleConverter()

    @Test
    fun shouldCorrectlyConvertEnums(){
        val userMapping = converter.fromRole(Role.USER)
        val moderatorMapping = converter.fromRole(Role.MODERATOR)
        val adminMapping = converter.fromRole(Role.ADMINISTRATOR)
        Assert.assertTrue(Role.USER == converter.fromString(userMapping))
        Assert.assertTrue(Role.MODERATOR == converter.fromString(moderatorMapping))
        Assert.assertTrue(Role.ADMINISTRATOR == converter.fromString(adminMapping))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowException(){
        converter.fromString("OE")
    }
}