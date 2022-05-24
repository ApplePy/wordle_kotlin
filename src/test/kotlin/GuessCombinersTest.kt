import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Path

internal class GuessCombinersTest {
    @Test
    internal fun createGoodLetterPredicate_noGuesses_allPass() {
        val goodLetterPredicate = createGoodLetterPredicate(emptySet())
        assertThat(goodLetterPredicate.invoke("")).isTrue()
        assertThat(goodLetterPredicate.invoke("abc")).isTrue()
    }

    @Test
    internal fun createGoodLetterPredicate_nonLocationGuess_allStringsContainingLetterPass() {
        val goodLetterPredicate = createGoodLetterPredicate(Guess('a'))
        assertThat(goodLetterPredicate.invoke("")).isFalse()
        assertThat(goodLetterPredicate.invoke("abc")).isTrue()
        assertThat(goodLetterPredicate.invoke("bac")).isTrue()
        assertThat(goodLetterPredicate.invoke("bc")).isFalse()
    }

    @Test
    internal fun createGoodLetterPredicate_locationGuess_onlyStringsContainingPositionedLetterPass() {
        val goodLetterPredicate = createGoodLetterPredicate(Guess('a', 1))
        assertThat(goodLetterPredicate.invoke("")).isFalse()
        assertThat(goodLetterPredicate.invoke("abc")).isFalse()
        assertThat(goodLetterPredicate.invoke("bac")).isTrue()
        assertThat(goodLetterPredicate.invoke("aac")).isTrue()
        assertThat(goodLetterPredicate.invoke("bc")).isFalse()
    }


    @Test
    internal fun createGoodLetterPredicate_nonLocationGuess_allStringsContainingMatchingBothPass() {
        val goodLetterPredicate = createGoodLetterPredicate(setOf(Guess('a'), Guess('b', 1)))
        assertThat(goodLetterPredicate.invoke("")).isFalse()
        assertThat(goodLetterPredicate.invoke("abc")).isTrue()
        assertThat(goodLetterPredicate.invoke("bac")).isFalse()
        assertThat(goodLetterPredicate.invoke("bc")).isFalse()
        assertThat(goodLetterPredicate.invoke("ac")).isFalse()
    }

    @Test
    internal fun createGoodLetterPredicate_multipleGuesses_onlyGuessesContainingPositionedLetterPass() {
        val goodLetterPredicate = createGoodLetterPredicate(Guess('a', 1))
        assertThat(goodLetterPredicate.invoke("")).isFalse()
        assertThat(goodLetterPredicate.invoke("abc")).isFalse()
        assertThat(goodLetterPredicate.invoke("bac")).isTrue()
        assertThat(goodLetterPredicate.invoke("aac")).isTrue()
        assertThat(goodLetterPredicate.invoke("bc")).isFalse()
    }

    @Test
    internal fun createBadLetterPredicate_noBadLetters_everythingPasses() {
        val badLetterPredicate = createBadLetterPredicate(setOf())
        assertThat(badLetterPredicate.invoke("")).isTrue()
        assertThat(badLetterPredicate.invoke("abc")).isTrue()
    }

    @Test
    internal fun createBadLetterPredicate_oneBadLetter_casesWithoutLetterPasses() {
        val badLetterPredicate = createBadLetterPredicate('a')
        assertThat(badLetterPredicate.invoke("")).isTrue()
        assertThat(badLetterPredicate.invoke("abc")).isFalse()
        assertThat(badLetterPredicate.invoke("bc")).isTrue()
    }

    @Test
    internal fun createBadLetterPredicate_multipleBadLetters_casesWithoutBothLettersPasses() {
        val badLetterPredicate = createBadLetterPredicate(setOf('a', 'b'))
        assertThat(badLetterPredicate.invoke("")).isTrue()
        assertThat(badLetterPredicate.invoke("abc")).isFalse()
        assertThat(badLetterPredicate.invoke("bc")).isFalse()
        assertThat(badLetterPredicate.invoke("ac")).isFalse()
        assertThat(badLetterPredicate.invoke("cd")).isTrue()
    }
}