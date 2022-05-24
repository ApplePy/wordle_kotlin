import java.io.BufferedReader
import java.io.FileReader
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.streams.asSequence

class WordList private constructor(private val words: Sequence<String>) {

    companion object {
        private val pattern = Pattern.compile("[a-zA-Z]{5}")

        fun create(wordListPath: Path): WordList {
            val wordList = sequence {
                BufferedReader(FileReader(wordListPath.toFile())).use {
                    yieldAll(
                        it.lines().asSequence().filter(pattern.asPredicate()::test).map(String::lowercase)
                            .toMutableSet()
                    )
                }
            }
            return WordList(wordList)
        }

        fun create(vararg words: String): WordList {
            val wordList = words.filter(pattern.asPredicate()::test).map(String::lowercase).asSequence()
            return WordList(wordList)
        }

        fun create(wordsIterable: Iterable<String>): WordList {
            val wordList = wordsIterable.filter(pattern.asPredicate()::test).map(String::lowercase).asSequence()
            return WordList(wordList)
        }
    }

    val remainingWords: Set<String>
        get() = _remainingWords.toSet()

    private val _remainingWords: MutableSet<String> by lazy {
        words.toMutableSet()
    }

    fun filterWordList(predicate: (String) -> Boolean): WordList {
        _remainingWords.retainAll(predicate)
        return this
    }
}