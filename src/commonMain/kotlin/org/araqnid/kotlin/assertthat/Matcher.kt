package org.araqnid.kotlin.assertthat

import kotlin.reflect.KProperty1

interface Matcher<in T> : SelfDescribing {
    fun match(actual: T): AssertionResult

    val negatedDescription
        get() = "not $description"

    operator fun not(): Matcher<T> = Negation(this)

    fun asPredicate(): (T) -> Boolean = { this.match(it) == AssertionResult.Match }

    companion object {
        operator fun <T> invoke(property: KProperty1<T, Boolean>): Matcher<T> = PredicateMatcher(property.name, property)

        operator fun <T> invoke(name: String, feature: (T) -> Boolean): Matcher<T> = PredicateMatcher(name, feature)
    }
}

fun <T> Matcher<T>.describedBy(fn: () -> String): Matcher<T> {
    return object : Matcher<T> by this {
        override val description: String
            get() = fn()
    }
}

sealed class AssertionResult {
    object Match : AssertionResult() {
        override fun toString() = "Match"
    }

    class Mismatch(override val description: String) : AssertionResult(), SelfDescribing {
        fun mapMessage(fn: (String) -> String) = Mismatch(fn(description))
        override fun toString() = "Mismatch[${describe(description)}]"
    }
}

val anything = object : Matcher<Any?> {
    override fun match(actual: Any?): AssertionResult = AssertionResult.Match
    override val description = "anything"
}

infix fun <T> Matcher<T>.or(that: Matcher<T>): Matcher<T> = Disjunction(this, that)
infix fun <T> Matcher<T>.and(that: Matcher<T>): Matcher<T> = Conjunction(this, that)

fun <T> allOf(matchers: List<Matcher<T>>): Matcher<T> =
    if (matchers.isEmpty()) anything else matchers.reduce { l, r -> l and r }

fun <T> anyOf(matchers: List<Matcher<T>>): Matcher<T> =
    if (matchers.isEmpty()) anything else matchers.reduce { l, r -> l or r }

fun <T> allOf(vararg matchers: Matcher<T>): Matcher<T> = allOf(matchers.toList())
fun <T> anyOf(vararg matchers: Matcher<T>): Matcher<T> = anyOf(matchers.toList())

private class Negation<in T>(private val negated: Matcher<T>) : Matcher<T> {
    override fun match(actual: T): AssertionResult {
        return when (negated.match(actual)) {
            AssertionResult.Match -> AssertionResult.Mismatch(negatedDescription)
            is AssertionResult.Mismatch -> AssertionResult.Match
        }
    }

    override val description: String
        get() = negated.negatedDescription
    override val negatedDescription: String
        get() = negated.description

    override fun not() = negated
}

private class Disjunction<in T>(private val left: Matcher<T>, private val right: Matcher<T>) : Matcher<T> {
    override fun match(actual: T): AssertionResult {
        val leftResult = left.match(actual)
        if (leftResult == AssertionResult.Match)
            return AssertionResult.Match
        return right.match(actual)
    }

    override val description: String
        get() = "${left.description} or ${right.description}"
}

private class Conjunction<in T>(private val left: Matcher<T>, private val right: Matcher<T>) : Matcher<T> {
    override fun match(actual: T): AssertionResult {
        val leftResult = left.match(actual)
        if (leftResult is AssertionResult.Mismatch)
            return leftResult
        return right.match(actual)
    }

    override val description: String
        get() = "${left.description} and ${right.description}"
}
