# Parsek

Parsek is a library for (and written in) Kotlin for easily building parser combinators. It is based on JParsec and (Haskell) Parsec.

It allows you to create a text (or token) parser based on easy to combine building blocks.

## What is a parser combinator?
A parser combinator lets you build complex parsers by combining simple ones. Each parser matches a small part of the input, and you can chain or nest them to parse more advanced patterns. This makes it easy to write readable and maintainable parsers for structured text.

## Examples

### Parse a digit
```kotlin
val parser = digit
parser("5abc") shouldBe "5"
```

### Parse a number (sequence of digits)
```kotlin
val parser = number
parser("123abc") shouldBe 123
```

### Parse an identifier (letter followed by letters/digits)
```kotlin
val identifier = letter and some(letter or digit)
identifier("abc123") shouldBe "abc123"
```

### Parse a signed number (e.g. "-42")
```kotlin
val parser = signedNumber
parser("-42") shouldBe -42
parser("17") shouldBe 17
```
