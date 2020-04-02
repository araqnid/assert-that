package org.araqnid.kotlin.assertthat

val isAbsent = object : Matcher<Any?> {
    override fun match(actual: Any?): AssertionResult {
        if (actual == null) return AssertionResult.Match
        return AssertionResult.Mismatch("was: ${describe(actual)}")
    }

    override val description = "null"
}

fun <T : Any> present(valueMatcher: Matcher<T>) = object : Matcher<T?> {
    override fun match(actual: T?): AssertionResult {
        if (actual == null) return AssertionResult.Mismatch("was null")
        return valueMatcher.match(actual)
    }

    override val description: String
        get() = valueMatcher.description
}

fun <T> equalTo(value: T) = object : Matcher<T?> {
    override fun match(actual: T?): AssertionResult {
        if (actual == value) return AssertionResult.Match
        return AssertionResult.Mismatch("was ${describe(actual)}")
    }

    override val description: String
        get() = "is equal to ${describe(value)}"

    override val negatedDescription: String
        get() = "is not equal to ${describe(value)}"
}

fun <T> isEqualTo(value: T) = object : Matcher<Any?> {
    override fun match(actual: Any?): AssertionResult {
        if (actual == value) return AssertionResult.Match
        return AssertionResult.Mismatch("was ${describe(actual)}")
    }

    override val description: String
        get() = "is equal to ${describe(value)}"

    override val negatedDescription: String
        get() = "is not equal to ${describe(value)}"
}

fun <T> sameInstance(value: T) = object : Matcher<T> {
    override fun match(actual: T): AssertionResult {
        if (actual === value) return AssertionResult.Match
        return AssertionResult.Mismatch("was ${describe(actual)}")
    }

    override val description: String
        get() = "is same instance as ${describe(value)}"

    override val negatedDescription: String
        get() = "is not same instance as ${describe(value)}"
}
