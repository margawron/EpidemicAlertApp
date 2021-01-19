package com.github.margawron.epidemicalertapp

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    RoleConverterTests::class,
    SuspicionLevelConverterTests::class,
    PeriodResolutionConverterTests::class,
    InstantConverterTests::class,
    AccountStateConverterTests::class,
)
class EpidemicAlertTestSuite {
}