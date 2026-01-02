package puzzle.core.frontend.lexer.recognition

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.token.PzlToken

sealed interface TokenRecognition {
	
	context(_: PzlContext)
	fun tryParse(input: CharArray, start: Int): PzlToken?
}