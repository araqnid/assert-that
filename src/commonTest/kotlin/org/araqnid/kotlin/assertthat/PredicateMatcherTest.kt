package org.araqnid.kotlin.assertthat

import kotlin.test.Test
import kotlin.test.assertEquals

class PredicateMatcherTest {
    @Test
    fun matches_property_with_predicate_function() {
        val matcher = has(String::length, equalTo(3))
        assertEquals(AssertionResult.Match, matcher.match("red"))
        assertEquals(AssertionResult.Mismatch("had length that was 4"), matcher.match("blue"))
        assertEquals("has length that is equal to 3", describe(matcher))
    }
}
