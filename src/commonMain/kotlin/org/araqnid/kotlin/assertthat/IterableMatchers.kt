package org.araqnid.kotlin.assertthat

fun <T> producesIterable(description: String, body: suspend SequenceMatcherScope<T>.() -> Unit): Matcher<Iterable<T>> {
    return object : Matcher<Iterable<T>> {
        override fun match(actual: Iterable<T>): AssertionResult {
            return producesSequence(description, body).match(actual.asSequence())
        }

        override val description: String = description
    }
}
