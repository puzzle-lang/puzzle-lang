package puzzle.core.lexer.recognizer

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlToken

sealed interface TokenRecognizer {
	
	context(_: PzlContext)
	fun tryParse(input: CharArray, start: Int): PzlToken?
}