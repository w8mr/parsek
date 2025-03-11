package nl.w8mr.parsek.symbol

import nl.w8mr.parsek.text.value

operator fun <R> String.rem(value: R) = this.value(value)
operator fun <R> Char.rem(value: R) = this.value(value)
