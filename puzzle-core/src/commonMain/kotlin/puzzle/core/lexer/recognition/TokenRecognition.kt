package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken

sealed interface TokenRecognition {
	
	context(_: PzlContext)
	fun tryParse(input: CharArray, start: Int): PzlToken?
}