data class Guess(val letter: Char, val pos: Int = -1) {
    fun asPredicate(): (String) -> Boolean = { str ->
        when {
            pos != -1 && str.length > pos -> str[pos] == letter
            pos == -1 -> str.indexOf(letter) != -1
            else -> false
        }
    }
}
