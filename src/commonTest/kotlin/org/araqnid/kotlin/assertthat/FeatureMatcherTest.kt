package org.araqnid.kotlin.assertthat

import kotlin.test.Test
import kotlin.test.assertEquals

class FeatureMatcherTest {
    @Test
    fun matches_feature_of_object() {
        val matcher = has("length", String::length, equalTo(3))
        assertEquals(AssertionResult.Match, matcher.match("red"))
        assertEquals(AssertionResult.Mismatch("had length that was 4"), matcher.match("blue"))
        assertEquals("has length that is equal to 3", describe(matcher))
    }
}
