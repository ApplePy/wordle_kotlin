fun createBadLetterPredicate(vararg badLetters: Char) = createBadLetterPredicate(badLetters.toSet())

fun createBadLetterPredicate(badLetters: Set<Char>): (String) -> Boolean {
    return { str -> badLetters.all { char -> str.indexOf(char) == -1 } }
}

fun createGoodLetterPredicate(vararg goodGuesses: Guess) = createGoodLetterPredicate(goodGuesses.toSet())

fun createGoodLetterPredicate(goodGuesses: Set<Guess>): (String) -> Boolean {
    return goodGuesses.map(Guess::asPredicate).reduceOrNull { a, b -> a.and(b) } ?: { true }
}

fun <T> ((T) -> Boolean).and(b: (T) -> Boolean): (T) -> Boolean {
    return { invoke(it) && b.invoke(it) }
}