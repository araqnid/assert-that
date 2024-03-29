# Assert-That

[ ![Kotlin](https://img.shields.io/badge/kotlin-1.5.32-blue.svg)](http://kotlinlang.org)
[![Kotlin JS IR supported](https://img.shields.io/badge/Kotlin%2FJS-IR%20supported-yellow)](https://kotl.in/jsirsupported)
[![Gradle Build](https://github.com/araqnid/assert-that/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/araqnid/assert-that/actions/workflows/gradle-build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.araqnid.kotlin.assert-that/assert-that.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.araqnid.kotlin.assert-that%22%20AND%20a%3A%22assert-that%22)

Kotlin multiplatform test assertion library inspired by [Hamkrest](https://github.com/npryce/hamkrest), itself
a Kotlinisation of [Hamcrest](https://github.com/hamcrest/JavaHamcrest)

## Usage

Provides a `Matcher` interface and an `assertThat` function, and a selection of `Matcher` implementations that
are intended to compare a value to an expectation and manufacture a helpful diagnostic if it does not.

### Matching on equality

For example, the matcher produced by `equalTo("foo")` will match only the string "foo". If you write:

```kotlin
assertThat("bar", equalTo("foo"))
```

You will get an assertion error like so:

```
AssertionError: expected: a value that is equal to "foo"
but was "bar"
```

`equalTo` is unusual in that it will match any type of object (because all objects can be compared for equality),
including nullable ones. `Matcher` is parameterised, and assertions may be more type-safe.

### Matching on features

You can match a property of an object using a `has` matcher:

```kotlin
assertThat("quux", has(String::length, equalTo(3)))
```

produces:

```
AssertionError: expected: a value that has length that is equal to 3
but had length that was 4
```

You can either define your own extension property, or use an expanded version to extract
a feature value:

```kotlin
assertThat("quux", has("length", { it.length }, equalTo(3)))
```

### Combining matchers

Matchers can be combined with the `and` and `or` infix functions, and inverted with the
negation operator:

#### Negation

```kotlin
assertThat("mumble", !equalTo("mumble"))
```

```
AssertionError: expected: a value that is not equal to "mumble"
but is equal to "mumble"
```

#### Combination

```kotlin
assertThat("womble", has(String::length, equalTo(5)) or has(String::length, equalTo(7)))
```

```
AssertionError: expected: a value that has length that is equal to 5 or has length that is equal to 7
but had length that was 6
```

Or more simply:

```kotlin
assertThat("womble", has(String::length, equalTo(5) or equalTo(7)))
```

```
AssertionError: expected: a value that has length that is equal to 5 or is equal to 7
but had length that was 6
```

Get the library
---------------

Assert-That is published on [Maven Central](https://search.maven.org). You need something like this in
`build.gradle` or `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    testImplementation("org.araqnid.kotlin.assert-that:assert-that:0.1.1")
}
```
