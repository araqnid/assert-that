package org.araqnid.kotlin.assertthat

private class FeatureMatcher<T, R>(
    private val name: String,
    private val feature: (T) -> R,
    private val featureMatcher: Matcher<R>
) :
    Matcher<T> {
    override fun match(actual: T): AssertionResult {
        val result = featureMatcher.match(feature(actual))
        if (result is AssertionResult.Mismatch) {
            return AssertionResult.Mismatch("had $name that ${result.description}")
        }
        return AssertionResult.Match
    }

    override val description = "has $name that ${featureMatcher.description}"
    override val negatedDescription = "does not have $name that ${featureMatcher.description}"
}

fun <T, R> has(name: String, feature: (T) -> R, featureMatcher: Matcher<R>): Matcher<T> =
    FeatureMatcher(name, feature, featureMatcher)
