# Parsek

[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/w8mr/parsek/.github%2Fworkflows%2Frelease.yaml)](https://github.com/w8mr/parsek/actions)
[![Maven Central](https://img.shields.io/maven-central/v/nl.w8mr.parsek/core)](https://mvnrepository.com/artifact/nl.w8mr.parsek/core/latest)
[![License](https://img.shields.io/github/license/w8mr/parsek)](LICENSE)

Parsek is a library for (and written in) Kotlin for easily building parser combinators. It is based on JParsec and (Haskell) Parsec. It allows you to create a text (or token) parser based on easy-to-combine building blocks.

---

## Table of Contents

- [What is Parsek?](#what-is-parsek)
- [What can Parsek be used for?](#what-can-parsek-be-used-for)
- [Installation](#installation)
- [Quick Start Guide](#quick-start-guide)
- [Core Concepts](#core-concepts)
- [Examples](#examples)
- [Error Handling](#error-handling)
- [Performance Notes](#performance-notes)
- [Related Projects](#related-projects)
- [Contribution Guidelines](#contribution-guidelines)
- [License](#license)
- [Links & Documentation](#links--documentation)

---

## What is Parsek?

Parsek is a functional parser combinator library that provides tools to construct complex parsers by combining smaller, reusable components. It is designed to be:

- **Declarative:** Define parsers in a readable and composable way.
- **Flexible:** Parse both text and token streams.
- **Lightweight:** Minimal dependencies and easy to integrate into Kotlin projects.

---

## What can Parsek be used for?

Parsek is versatile and can be used for a variety of parsing tasks, such as:

- **Parsing Configuration Files:** JSON, YAML, or custom formats.
- **Building DSLs:** Create interpreters or compilers for domain-specific languages.
- **Processing Structured Data:** Parse CSV, logs, or other structured text.
- **Advent of Code Challenges:** Quickly write parsers for puzzle inputs.
- **Language Parsing:** Build lexers and parsers for programming languages.

---


## Installation

Add Parsek to your project using **Gradle** or **Maven**.

### Gradle

```groovy
dependencies {
    implementation 'nl.w8mr.parsek:core:<latest-version>'
}
```

### Maven

```xml
<dependency>
  <groupId>nl.w8mr.parsek</groupId>
  <artifactId>core</artifactId>
  <version><!-- latest-version --></version>
</dependency>
```

Replace `<latest-version>` with the version shown in the badge above.

---

## Quick Start Guide

Here's a minimal example to get you started:

```kotlin
//import nl.w8mr.parsek.text.*

val parser = number // Parses a sequence of digits as an Int
val result = parser("123abc") // result: 123
```

You can also combine parsers:

```kotlin
val signed = signedNumber
println(signed("-42")) // Output: -42
println(signed("17"))  // Output: 17
```

---

## Core Concepts

### General Parsing

At its core, Parsek operates on the concept of a `Parser`. A `Parser` is a function that takes an input, consumes a part of it, and returns a result along with the remaining input. The result can either be a success or a failure.

```kotlin
interface Parser<Token, R> {
    fun apply(source: ParserSource<Token>): Result<R>

    sealed class Result<R> {
        data class Success<R>(val value: R) : Result<R>()
        data class Failure<R>(val message: String) : Result<R>()
    }
}
```

### Text-Specific Parsing

While the core library is generic, the `nl.w8mr.parsek.text` package provides utilities specifically for parsing `CharSequence` (e.g., `String`). These text-specific parsers simplify common tasks like matching characters, strings, or patterns.

For example, instead of writing a generic parser for a specific character, you can use the `char` function from the `text` package:

```kotlin
val digit = char { it.isDigit() }
```

#### Key Features of Text-Specific Parsing

1. **String Output:** All text parsers produce output as `String`. This ensures consistency when working with text-based data.
2. **Automatic Concatenation:** When a parser produces a list of strings (e.g., from a `repeat` or `sepBy` combinator), the result is automatically concatenated into a single string.

For example:

```kotlin
val digit = char { it.isDigit() }
val digits = repeat(digit)

// Input: "123abc"
// Output: "123" (list of digits concatenated into a single string)
digits("123abc")
```

There are also predefined parsers for common patterns, such as `digit`, `letter`, `number`, and more.

---

## Examples

### Parse a Digit

```kotlin
val parser = digit
parser("5abc") shouldBe "5"
```

### Parse a Number (Sequence of Digits)

```kotlin
val parser = number
parser("123abc") shouldBe 123
```

### Parse an Identifier (Letter Followed by Letters/Digits)

```kotlin
val identifier = letter and some(letter or digit)
identifier("abc123") shouldBe "abc123"
```

### Parse a Signed Number (e.g. "-42")

```kotlin
val parser = signedNumber
parser("-42") shouldBe -42
parser("17") shouldBe 17
```

---

### Parsing a Comma-Separated List of Numbers

#### General Parsing

```kotlin
val digit = char { it.isDigit() }
val number = repeat(digit, min = 1) map { it.joinToString("").toInt() }
val comma = char(',')
val numberList = number sepBy comma

numberList("123,45,6") // Success: ([123, 45, 6], "")
```

#### Text-Specific Parsing

Using the `text` package, the same parser can be written more concisely:

```kotlin
val numberList = string("123,45,6")
numberList("123,45,6") // Success: ([123, 45, 6], "")
```

---

### Parsing Nested Structures

#### General Parsing

```kotlin
val openBracket = literal('[')
val closeBracket = literal(']')
val comma = char(',')
val number = repeat(char { it.isDigit() }, min = 1) map { it.toInt() }
val value = ref(::list) or number
val list: Parser<Char, List<Any>> = openBracket and (value sepBy comma) and closeBracket

list("[1,[2,3],4]") shouldBe 
        listOf(1, listOf(2, 3), 4)
```

#### Text-Specific Parsing

```kotlin
val list = string("[1,[2,3],4]")
list("[1,[2,3],4]") // Success: ([1, [2, 3], 4], "")
```

---

## Error Handling

Parsek provides robust error handling through its `Result` class. A parser can return either:

- **Success:** Contains the parsed value.
- **Failure:** Contains an error message describing what went wrong.

For example:

```kotlin
val parser = char('a')
parser("b") // Failure: Expected 'a', but found 'b'.
```

---

## Distinction Between General and Text-Specific Parsing

### General Parsing

General parsers operate on any sequence of tokens. You define what a "token" is, making these parsers highly flexible for non-text inputs (e.g., token streams from a lexer).

### Text-Specific Parsing

Text parsers are optimized for `CharSequence` inputs. They provide utilities for common text-parsing tasks, such as matching characters, strings, or patterns. These parsers are more concise and easier to use for text-based grammars.

---

## Combinator DSL

The combinator DSL in Parsek provides a powerful and expressive way to define parsers. It allows you to combine multiple parsers into a single cohesive unit, producing a structured output object. Additionally, the DSL provides mechanisms to handle failure responses explicitly, enabling custom error handling and recovery strategies.

### What are `combi` and `bind`?

- **`combi`**: A DSL block for combining multiple parsers, handling their results and errors in a structured way.
- **`bind`**: Used inside a `combi` block to run a parser and extract its result, or fail if the parser fails.

#### Example: Parsing a Key-Value Pair

```kotlin
val keyValueParser = combi<Char, Pair<String, String>> {
    val key = repeat(char { it.isLetterOrDigit() }).bind()
    -char('=')
    val value = repeat(char { it.isLetterOrDigit() || it == ' ' }, min = 1).bind()
    key to value
}

keyValueParser("username=John Doe") // Success: ("username" to "John Doe")
```

In this example:
- The `key` parser extracts the key (e.g., `username`).
- The `value` parser extracts the value (e.g., `John Doe`).
- The result is combined into a `Pair` object.

### Handling Failure Responses

The combinator DSL also allows you to handle failure responses explicitly. This is useful when you want to provide custom error messages or fallback behavior.

#### Example: Custom Error Handling

```kotlin
val safeKeyValueParser = combi {
    val key = repeat(char { it.isLetterOrDigit() }).bind()
    if (key.isEmpty()) {
        fail("Key cannot be empty")
    }
    -char('=')
    val value = repeat(char { it.isLetterOrDigit() || it == ' ' }, min = 1).bind()
    if (value.isEmpty()) {
        fail("Value cannot be empty")
    }
    key to value
}

val result = safeKeyValueParser("=John Doe")
if (result is Parser.Failure) {
    println("Parsing failed: ${result.message}")
}
```

### Using `bindAsResult` and `Result.bind` for Custom Failure Handling

The `bindAsResult` and `Result.bind` methods allow you to customize how failures are handled within a parser. These methods are particularly useful when you want to propagate or transform failure results explicitly.

#### Example: Parsing a Repeated Pattern

```kotlin
val repeatedParser = combi {
    val list = mutableListOf<String>()
    val parser = char { it.isLetter() }

    while (list.size < 3) { // Ensure at least 3 elements are parsed
        when (val result = parser.bindAsResult()) {
            is Parser.Success -> list.add(result.bind())
            is Parser.Failure -> fail("Only ${list.size} elements found, needed at least 3")
        }
    }

    while (list.size < 5) { // Parse up to 5 elements
        when (val result = parser.bindAsResult()) {
            is Parser.Success -> list.add(result.bind())
            is Parser.Failure -> break
        }
    }

    list
}

repeatedParser("abcde") shouldBe listOf("a", "b", "c", "d", "e")

shouldThrowMessage<ParseException>("Combinator failed, parser number 3 with error: Only 2 elements found, needed at least 3") {
    repeatedParser("ab") shouldBe listOf("a", "b")
}
```

---

## Performance Notes

Parsek is designed to be lightweight and efficient for most parsing tasks. For very large inputs or performance-critical applications, consider benchmarking against alternatives. Contributions with benchmarks are welcome!

---

## Related Projects

- [JParsec](https://github.com/jparsec/jparsec) - Java parser combinator library
- [Parsec (Haskell)](https://hackage.haskell.org/package/parsec) - Haskell parser combinator library
- [KotlinParsec](https://github.com/VerbalExpressions/KotlinParsec) - Another Kotlin parser combinator library

---

## Contribution Guidelines

We welcome contributions! To get started:

1. **Fork the repository** and clone it locally.
2. **Set up the development environment**:
    - Requires [Kotlin](https://kotlinlang.org/) and [Gradle](https://gradle.org/).
    - Run tests with `./gradlew test`.
3. **Create a feature branch** for your changes.
4. **Submit a pull request** with a clear description.

Please follow the [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html) and write tests for new features.

---

## License

Parsek is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## Links & Documentation

- [GitHub Repository](https://github.com/w8mr/parsek)

---

