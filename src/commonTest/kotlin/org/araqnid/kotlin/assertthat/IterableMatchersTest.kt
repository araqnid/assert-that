package org.araqnid.kotlin.assertthat

import kotlin.test.Test
import kotlin.test.assertEquals

class IterableMatchersTest {
    @Test
    fun match_iterable_with_coroutine_item_by_item() {
        val matcher = producesIterable<String>("sequence of \"red\" matched by coroutine") {
            assertNext(equalTo("red"))
        }

        assertEquals(AssertionResult.Match, matcher.match(listOf("red")))
        assertEquals(AssertionResult.Mismatch("item #0 not available (expected is equal to \"red\")"), matcher.match(emptyList()))
        assertEquals(AssertionResult.Mismatch("item #0 was \"blue\""), matcher.match(listOf("blue")))
        assertEquals(AssertionResult.Mismatch("produced additional items: [\"blue\"]"), matcher.match(listOf("red", "blue")))
        assertEquals("sequence of \"red\" matched by coroutine", matcher.description)
    }

    @Test
    fun match_iterable_with_coroutine_matching_remaining_items() {
        val matcher = producesIterable<String>("sequence of \"red\" followed by 2 items") {
            assertNext(equalTo("red"))
            assertRemaining(hasSize(2))
        }

        assertEquals(AssertionResult.Match, matcher.match(listOf("red", "x", "y")))
        assertEquals(AssertionResult.Mismatch("remaining items had size that was 1"), matcher.match(listOf("red", "blue")))
        assertEquals("sequence of \"red\" followed by 2 items", matcher.description)
    }
}
