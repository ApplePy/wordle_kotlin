import java.nio.file.Path
import kotlin.io.path.exists

private data class Guesses(
    val badChars: MutableSet<Char> = mutableSetOf(), val goodGuesses: MutableSet<Guess> = mutableSetOf()
)

fun main(args: Array<String>) {
    println("Welcome to Wordle Solver")

    val wordList = WordList.create(getDictionaryPath(args))
    val guesses = Guesses()

    printMenu(addBadCharacter = { guesses.badChars.add(addBadCharacter(wordList)) },
        addGoodCharacter = { guesses.goodGuesses.add(addGoodCharacter(wordList)) },
        printOptions = {
            print("Possible solutions: ")
            println(wordList.remainingWords)
        },
        printGuesses = {
            print("Guesses: ")
            println(guesses)
        })

    println("Goodbye.")
}

private fun printMenu(
    addBadCharacter: () -> Unit, addGoodCharacter: () -> Unit, printOptions: () -> Unit, printGuesses: () -> Unit
) {
    while (true) {
        println("Options:")
        println("1) Enter bad character")
        println("2) Enter good character")
        println("3) See possible words")
        println("4) See current hints")
        println("5) Quit")
        print("Select option: ")

        when (readln()) {
            "1" -> addBadCharacter.invoke()
            "2" -> addGoodCharacter.invoke()
            "3" -> printOptions.invoke()
            "4" -> printGuesses.invoke()
            "5" -> break
            else -> println("Invalid option")
        }
    }
}

private fun addBadCharacter(wordList: WordList): Char {
    var input: String
    do {
        print("Specify bad character: ")
        input = readln().lowercase()
    } while (input.length != 1)

    val char = input[0]
    wordList.filterWordList(createBadLetterPredicate(char))
    return char
}

private fun addGoodCharacter(wordList: WordList): Guess {
    var input: String
    do {
        print("Specify good character: ")
        input = readln().lowercase()
    } while (input.length != 1)
    val char = input[0]

    var intInput: Int?
    do {
        print("Specify position (or empty for none): ")
        intInput = readln().toIntOrNull() ?: -1
        // TODO: Remove length hardcoding
        if (intInput < 1 || intInput > 5) {
            println("Invalid position.")
            intInput = null
        }
        intInput = intInput?.minus(1)
    } while (intInput == null)

    var posOrBad: Boolean? = null
    while (intInput != -1 && posOrBad == null) {
        print("Good position? (Y/N) ")
        input = readln()
        when {
            input.lowercase() == "y" -> posOrBad = true
            input.lowercase() == "n" -> posOrBad = false
            else -> println("Invalid input")
        }
    }

    val guess = if (posOrBad == true) Guess(char, pos = intInput) else Guess(char, badPos = intInput)
    wordList.filterWordList(createGoodLetterPredicate(guess))
    return guess
}

private fun getDictionaryPath(args: Array<String>): Path {
    var wordPath = ""
    if (args.isNotEmpty()) {
        wordPath = args[0]
    }

    if (wordPath == "" || !Path.of(wordPath).exists()) {
        do {
            print("Please provide URI of dictionary text file: ")
            wordPath = readLine() ?: ""
            val pathExists = wordPath != "" && Path.of(wordPath).exists()
            if (!pathExists) {
                println("Invalid URI, try again")
            }
        } while (!pathExists)
    }
    return Path.of(wordPath)
}