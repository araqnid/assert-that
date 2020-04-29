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

    @Test
    fun match_sequence_with_coroutine_item_by_item() {
        val matcher = producesSequence<String>("sequence of \"red\" matched by coroutine") {
            assertNext(equalTo("red"))
        }

        assertEquals(AssertionResult.Match, matcher.match(sequenceOf("red")))
        assertEquals(AssertionResult.Mismatch("item #0 not available (expected is equal to \"red\")"), matcher.match(emptySequence()))
        assertEquals(AssertionResult.Mismatch("item #0 was \"blue\""), matcher.match(sequenceOf("blue")))
        assertEquals(AssertionResult.Mismatch("produced additional items: [\"blue\"]"), matcher.match(sequenceOf("red", "blue")))
        assertEquals("sequence of \"red\" matched by coroutine", matcher.description)
    }

    @Test
    fun match_sequence_with_coroutine_matching_remaining_items() {
        val matcher = producesSequence<String>("sequence of \"red\" followed by 2 items") {
            assertNext(equalTo("red"))
            assertRemaining(hasSize(2))
        }

        assertEquals(AssertionResult.Match, matcher.match(sequenceOf("red", "x", "y")))
        assertEquals(AssertionResult.Mismatch("remaining items had size that was 1"), matcher.match(sequenceOf("red", "blue")))
        assertEquals("sequence of \"red\" followed by 2 items", matcher.description)
    }
}
