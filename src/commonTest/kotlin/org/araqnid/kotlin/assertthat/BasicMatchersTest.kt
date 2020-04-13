package org.araqnid.kotlin.assertthat

import kotlin.test.Test
import kotlin.test.assertEquals

class BasicMatchersTest {
    @Test
    fun matches_null_only() {
        val absent: Matcher<Any?> = isAbsent
        assertEquals(AssertionResult.Match, absent.match(null))
        assertEquals(AssertionResult.Mismatch("was \"foo\""), absent.match("foo"))
        assertEquals("null", describe(absent))
    }

    @Test
    fun matches_non_null_only() {
        val matcher: Matcher<String?> = present(containsSubstring("foo"))
        assertEquals(AssertionResult.Match, matcher.match("foo"))
        assertEquals(AssertionResult.Mismatch("was null"), matcher.match(null))
        assertEquals(AssertionResult.Mismatch("was \"bar\""), matcher.match("bar"))
        assertEquals("contains \"foo\"", describe(matcher))
    }

    @Test
    fun matches_equal() {
        val aThing = Thing(1)
        val matcher: Matcher<Thing?> = equalTo(aThing)
        assertEquals(AssertionResult.Match, matcher.match(aThing))
        assertEquals(AssertionResult.Match, matcher.match(Thing(1)))
        assertEquals(AssertionResult.Mismatch("was null"), matcher.match(null))
        assertEquals(AssertionResult.Mismatch("was Thing(n=2)"), matcher.match(Thing(2)))
        assertEquals("is equal to Thing(n=1)", describe(matcher))
    }

    @Test
    fun matches_same_instance() {
        val aThing = Thing(1)
        val matcher: Matcher<Thing?> = sameInstance(aThing)
        assertEquals(AssertionResult.Match, matcher.match(aThing))
        assertEquals(AssertionResult.Mismatch("was null"), matcher.match(null))
        assertEquals(AssertionResult.Mismatch("was Thing(n=2)"), matcher.match(Thing(2)))
        assertEquals(AssertionResult.Mismatch("was Thing(n=1)"), matcher.match(Thing(1)))
        assertEquals("is same instance as Thing(n=1)", describe(matcher))
    }

    data class Thing(val n: Int)
}
