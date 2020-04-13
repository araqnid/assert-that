package org.araqnid.kotlin.assertthat

import kotlin.reflect.KProperty1

fun <T, R> has(property: KProperty1<T, R>, propertyMatcher: Matcher<R>): Matcher<T> =
    has(PredicateMatcher.identifierToDescription(property.name), property, propertyMatcher)

internal class PredicateMatcher<in T>(name: String, private val predicate: (T) -> Boolean) : Matcher<T> {
    override fun match(actual: T): AssertionResult {
        if (predicate(actual)) return AssertionResult.Match
        return AssertionResult.Mismatch("was ${describe(actual)}")
    }

    override val description = identifierToDescription(name)
    override val negatedDescription = identifierToNegatedDescription(name)
    override fun asPredicate() = predicate

    companion object {
        fun identifierToDescription(id: String): String {
            return identifierToWords(id).joinToString(" ")
        }

        fun identifierToNegatedDescription(id: String): String {
            val words = identifierToWords(id).iterator()
            val first = words.next()
            val rest = words.asSequence().joinToString(" ")

            return when (first) {
                "is" -> "is not $rest"
                "has" -> "does not have $rest"
                else -> "not $first $rest"
            }
        }

        fun identifierToWords(s: String) = sequence {
            val buf = StringBuilder()

            for ((prev, c) in (s[0] + s).zip(s)) {
                if (isWordStart(prev, c)) {
                    if (buf.isNotEmpty()) {
                        yield(buf.toString())
                        buf.clear()
                    }
                }

                if (isWordPart(c)) {
                    buf.append(c.toLowerCase())
                }
            }

            yield(buf.toString())
        }

        fun isWordPart(c: Char): Boolean = c.isLetterOrDigit()

        fun isWordStart(prev: Char, c: Char): Boolean = when {
            c.isLetter() != prev.isLetter() -> true
            prev.isLowerCase() && c.isUpperCase() -> true
            else -> false
        }

        fun Char.isLowerCase() = this in "abcdefghijklmnopqrstuvwxyz"
        fun Char.isUpperCase() = this in "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        fun Char.isLetter() = this.isLowerCase() || this.isUpperCase()
        fun Char.isLetterOrDigit() = this.isLetter() || this in "0123456789"
    }
}
