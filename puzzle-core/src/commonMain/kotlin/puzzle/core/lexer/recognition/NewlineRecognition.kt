package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.WhiteSpaceKind.NEWLINE

object NewlineRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val char = input[start]
		if (char != '\n') return null
		return PzlToken(NEWLINE, start span start + 1)
	}
}