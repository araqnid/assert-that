package org.araqnid.kotlin.assertthat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MatcherTest {
    @Test
    fun matcher_as_predicate() {
        assertTrue(anything.asPredicate()("foo"))
        assertFalse((!anything).asPredicate()("foo"))
    }

    @Test
    fun matcher_from_property() {
        val matcher: Matcher<String> = Matcher(String::isBlankAsProperty)
        assertEquals(AssertionResult.Match, matcher.match("   "))
        assertEquals(AssertionResult.Mismatch("was \"abc\""), matcher.match("abc"))
        assertEquals("is blank as property", describe(matcher))
    }

    @Test
    fun matcher_from_predicate() {
        val matcher: Matcher<String> = Matcher("isBlank", String::isBlank)
        assertEquals(AssertionResult.Match, matcher.match("   "))
        assertEquals(AssertionResult.Mismatch("was \"abc\""), matcher.match("abc"))
        assertEquals("is blank", describe(matcher))
    }

    @Test
    fun matcher_negation() {
        assertEquals(AssertionResult.Match, anything.match("foo"))
        assertEquals(AssertionResult.Mismatch("anything"), (!anything).match("foo"))
        assertEquals("not anything", describe(!anything))
    }

    @Test
    fun matcher_with_custom_description() {
        val anythingPlus = anything.describedBy { "custom anything" }
        assertEquals(AssertionResult.Match, anythingPlus.match("foo"))
        assertEquals(AssertionResult.Mismatch("custom anything"), (!anythingPlus).match("foo"))
        assertEquals("not custom anything", describe(!anythingPlus))
    }

    @Test
    fun matcher_and() {
        assertEquals(AssertionResult.Match, (anything and anything).match("foo"))
        assertEquals(AssertionResult.Mismatch("anything"), (anything and !anything).match("foo"))
        assertEquals("anything and not anything", describe(anything and !anything))
    }

    @Test
    fun matcher_allOf() {
        assertEquals(AssertionResult.Match, (allOf(anything, anything)).match("foo"))
        assertEquals(AssertionResult.Mismatch("anything"), allOf(anything, !anything).match("foo"))
        assertEquals("anything and not anything", describe(allOf(anything, !anything)))
    }

    @Test
    fun matcher_or() {
        assertEquals(AssertionResult.Match, anyOf(anything, !anything).match("foo"))
        assertEquals("anything or not anything", describe(anyOf(anything, !anything)))
    }
}

private val String.isBlankAsProperty
    get() = isBlank()
