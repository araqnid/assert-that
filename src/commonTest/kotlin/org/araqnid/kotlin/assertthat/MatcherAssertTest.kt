package org.araqnid.kotlin.assertthat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MatcherAssertTest {
    @Test
    fun asserts_that_matcher_matches() {
        assertThat("xyzzy", equalTo("xyzzy"))
        val exception = assertFailsWith<AssertionError> {
            assertThat("xyzzy", equalTo("abcde"))
        }
        assertEquals(
            "expected: a value that is equal to \"abcde\"\nbut was \"xyzzy\"", exception.message
        )
    }
}
