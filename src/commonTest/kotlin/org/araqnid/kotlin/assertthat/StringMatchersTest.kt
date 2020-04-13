package org.araqnid.kotlin.assertthat

import kotlin.test.Test
import kotlin.test.assertEquals

class StringMatchersTest {
    @Test
    fun match_containing_pattern() {
        val matcher: Matcher<CharSequence> = contains(Regex("fab"))
        assertEquals(AssertionResult.Match, matcher.match("confabulous"))
        assertEquals(AssertionResult.Mismatch("was \"contrary\""), matcher.match("contrary"))
        assertEquals("matches /fab/", describe(matcher))
    }

    @Test
    fun match_containing_substring() {
        val matcher: Matcher<CharSequence> = containsSubstring("fab")
        assertEquals(AssertionResult.Match, matcher.match("confabulous"))
        assertEquals(AssertionResult.Mismatch("was \"contrary\""), matcher.match("contrary"))
        assertEquals("contains \"fab\"", describe(matcher))
    }
}
