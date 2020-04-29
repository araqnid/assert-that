package org.araqnid.kotlin.assertthat

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.RestrictsSuspension
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine

fun <T> producesSequence(vararg matchers: Matcher<T>) = object : Matcher<Sequence<T>> {
    override fun match(actual: Sequence<T>): AssertionResult {
        val expectedIterator = matchers.iterator()
        val actualIterator = actual.iterator()
        var index = 0
        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            val itemMatcher = expectedIterator.next()
            val actualItem = actualIterator.next()
            val match = itemMatcher.match(actualItem)
            if (match != AssertionResult.Match) {
                return AssertionResult.Mismatch("item #$index ${describe(match)}")
            }
            ++index
        }
        if (expectedIterator.hasNext()) {
            return AssertionResult.Mismatch("expected more than $index items")
        }
        if (actualIterator.hasNext()) {
            return AssertionResult.Mismatch("produced more than $index items: ${describe(actualIterator.asSequence().toList())}")
        }
        return AssertionResult.Match
    }

    override val description: String = matchers.withIndex().joinToString(prefix = "sequence where ") { (index, matcher) -> "item #$index is ${describe(matcher)}" }
}

val producesEmptySequence = producesSequence<Any?>().describedBy { "empty sequence" }

@RestrictsSuspension
interface SequenceMatcherScope<out T> {
    suspend fun take(): T

    val index: Int

    suspend fun assertNext(matcher: Matcher<T>) {
        val value = try {
            take()
        } catch (e: InputExhaustedException) {
            throw SequenceMismatchException(AssertionResult.Mismatch("item #$index not available (expected ${describe(matcher)})"))
        }

        val match = matcher.match(value)
        if (match is AssertionResult.Mismatch)
            throw SequenceMismatchException(match.mapMessage { "item #$index $it" })
    }

    suspend fun assertRemaining(matcher: Matcher<List<T>>) {
        val values = mutableListOf<T>()
        try {
            while (true) {
                values += take()
            }
        } catch (e: InputExhaustedException) {
            // ignore
        }

        val match = matcher.match(values)
        if (match is AssertionResult.Mismatch)
            throw SequenceMismatchException(match.mapMessage { "remaining items $it" })
    }
}

suspend fun <T : Any> SequenceMatcherScope<T>.takeOrNull(): T? {
    return try {
        take()
    } catch (e: InputExhaustedException) {
        null
    }
}

class SequenceMismatchException(val mismatch: AssertionResult.Mismatch) : Exception()

class InputExhaustedException : IllegalStateException("Input exhausted")

fun <T> producesSequence(description: String, body: suspend SequenceMatcherScope<T>.() -> Unit): Matcher<Sequence<T>> {
    return object : Matcher<Sequence<T>> {
        override fun match(actual: Sequence<T>): AssertionResult {
            val scope = object : SequenceMatcherScope<T>, Continuation<Unit> {
                init {
                    body.startCoroutine(this, this)
                }

                override var index = 0
                var isFinished = false
                var failureException: Throwable? = null
                var continuation: Continuation<T>? = null

                override suspend fun take(): T {
                    return suspendCoroutine { cont ->
                        continuation = cont
                    }
                }

                override val context: CoroutineContext
                    get() = EmptyCoroutineContext

                override fun resumeWith(result: Result<Unit>) {
                    isFinished = true
                    failureException = result.exceptionOrNull()
                }

                private fun pullContinuation(): Continuation<T> {
                    val pulled = continuation
                    continuation = null
                    check(pulled != null) { "matcher should be waiting for input" }
                    return pulled
                }

                fun push(value: T): AssertionResult.Mismatch? {
                    check(!isFinished) { "matcher has already terminated" }
                    pullContinuation().resume(value)
                    ++index
                    return when {
                        failureException is SequenceMismatchException -> (failureException as SequenceMismatchException).mismatch
                        failureException != null -> throw failureException!!
                        else -> null
                    }
                }

                fun finish(): AssertionResult {
                    if (isFinished) return AssertionResult.Match

                    while (!isFinished) {
                        pullContinuation().resumeWithException(InputExhaustedException())
                    }

                    return when {
                        failureException is SequenceMismatchException -> (failureException as SequenceMismatchException).mismatch
                        failureException != null -> throw failureException!!
                        else -> AssertionResult.Match
                    }
                }
            }

            val iterator = actual.iterator()
            while (iterator.hasNext() && !scope.isFinished) {
                val result = scope.push(iterator.next())
                if (result != null) return result
            }
            if (iterator.hasNext()) {
                val residual = iterator.asSequence().toList()
                return AssertionResult.Mismatch("produced additional items: ${residual.map { describe(it) }}")
            }

            return scope.finish()
        }

        override val description: String = description
    }
}
