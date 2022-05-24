import java.lang.IllegalArgumentException

data class Guess(val letter: Char, val pos: Int = -1, val badPos: Int = 1) {
    init {
        if(pos != -1 && badPos != 1) throw IllegalArgumentException("pos and badPos can't both be defined")
    }
    fun asPredicate(): (String) -> Boolean = { str ->
        when {
            pos != -1 && str.length > pos -> str[pos] == letter
            badPos != -1 && str.length > badPos -> str[badPos] != letter && str.indexOf(letter) != -1
            pos == -1 && badPos == -1 -> str.indexOf(letter) != -1
            else -> false
        }
    }
}
