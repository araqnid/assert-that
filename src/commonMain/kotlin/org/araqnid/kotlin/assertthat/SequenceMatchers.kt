package org.araqnid.kotlin.assertthat

fun <T> producesSequence(vararg matchers: Matcher<T>) = object : Matcher<Sequence<T>> {
    override fun match(actual: Sequence<T>): AssertionResult {
        val expectedIterator = matchers.iterator()
        val actualIterator = actual.iterator()
        var index = 0
        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            val itemMatcher = expectedIterator.next()
            val actualItem = actualIterator.next()
            val match = itemMatcher.match(actualItem)
            if (match != AssertionResult.Match) {
                return AssertionResult.Mismatch("item #$index ${describe(match)}")
            }
            ++index
        }
        if (expectedIterator.hasNext()) {
            return AssertionResult.Mismatch("expected more than $index items")
        }
        if (actualIterator.hasNext()) {
            return AssertionResult.Mismatch("produced more than $index items: ${describe(actualIterator.asSequence().toList())}")
        }
        return AssertionResult.Match
    }

    override val description: String = matchers.withIndex().joinToString(prefix = "sequence where ") { (index, matcher) -> "item #$index is ${describe(matcher)}" }
}

val producesEmptySequence = producesSequence<Any?>().describedBy { "empty sequence" }
