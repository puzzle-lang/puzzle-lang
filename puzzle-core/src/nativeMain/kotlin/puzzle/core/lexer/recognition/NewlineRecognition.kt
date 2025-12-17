package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.WhiteSpaceKind
import puzzle.core.model.span

object NewlineRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val char = input[start]
		if (char != '\n') return null
		return PzlToken(WhiteSpaceKind.NEWLINE, start span start + 1)
	}
}