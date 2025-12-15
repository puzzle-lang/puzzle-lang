package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.token.WhiteSpaceKind

object NewlineRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val char = input[start]
		if (char != '\n') return null
		return PzlToken(WhiteSpaceKind.NEWLINE, start, start + 1)
	}
}