import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeLines

internal class WordListTest {

    private val initialWords: List<String> = listOf("apples", "banana", "koalas", "google")

    @Test
    fun createWithFile_returnsAllWords() {
        val tempDictionaryFile = createTempFile("wordlist", ".txt")
        try {
            tempDictionaryFile.writeLines(initialWords)
            val wordList = WordList.create(tempDictionaryFile)
            assertThat(wordList.remainingWords).containsExactlyElementsIn(initialWords)
        } finally{
            tempDictionaryFile.deleteIfExists()
        }
    }

    @Test
    fun createWithFile_withFilter_returnsFilteredWords() {
        val tempDictionaryFile = createTempFile("wordlist", ".txt")
        try {
            tempDictionaryFile.writeLines(initialWords)
            val wordList = WordList.create(tempDictionaryFile)
            wordList.filterWordList(createBadLetterPredicate('a'))
            assertThat(wordList.remainingWords).containsExactly("google")
        } finally{
            tempDictionaryFile.deleteIfExists()
        }
    }

    @Test
    fun getRemainingWords_noFiltering_returnsAllWords() {
        val wordList = WordList.create(initialWords)
        assertThat(wordList.remainingWords).containsExactlyElementsIn(initialWords)
    }

    @Test
    fun filterWordList_removeLetterA_returnsAllWordsMissingA() {
        val wordList = WordList.create(initialWords)
        wordList.filterWordList(createBadLetterPredicate('a'))
        assertThat(wordList.remainingWords).containsExactly("google")
    }

    @Test
    fun filterWordList_multipleFiltering_resultsPersistBetweenCalls() {
        val wordList = WordList.create(initialWords)
        wordList.filterWordList(createBadLetterPredicate('k'))
        assertThat(wordList.remainingWords).containsExactly("apples", "banana", "google")
        wordList.filterWordList(createBadLetterPredicate('b'))
        assertThat(wordList.remainingWords).containsExactly("apples", "google")
    }
}