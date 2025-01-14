package nl.w8mr.parsek.test

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

inline fun <reified T: Throwable> shouldThrowMessage(expectedMessage: String, failMessage: String = "", block: () -> Any) =
    assertEquals(expectedMessage, assertFailsWith<T>(failMessage, { block(); null }).message)