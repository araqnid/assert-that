package org.araqnid.kotlin.assertthat

import kotlin.test.Test
import kotlin.test.assertEquals

class SequenceMatchersTest {
    @Test
    fun match_empty_sequence() {
        val matcher: Matcher<Sequence<Any?>> = producesEmptySequence
        assertEquals(AssertionResult.Match, matcher.match(emptySequence()))
        assertEquals(AssertionResult.Mismatch("produced more than 0 items: [\"red\"]"), matcher.match(sequenceOf("red")))
        assertEquals("empty sequence", matcher.description)
    }

    @Test
    fun match_non_empty_sequence() {
        val matcher: Matcher<Sequence<String?>> = producesSequence(equalTo("red"))
        assertEquals(AssertionResult.Match, matcher.match(sequenceOf("red")))
        assertEquals(AssertionResult.Mismatch("expected more than 0 items"), matcher.match(emptySequence()))
        assertEquals(AssertionResult.Mismatch("item #0 was \"blue\""), matcher.match(sequenceOf("blue")))
        assertEquals(AssertionResult.Mismatch("produced more than 1 items: [\"blue\"]"), matcher.match(sequenceOf("red", "blue")))
        assertEquals("sequence where item #0 is is equal to \"red\"", matcher.description)
    }
}
