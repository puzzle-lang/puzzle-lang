package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.WhiteSpaceKind.TAB

object TabRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != '\t') return null
		var position = start + 1
		while (position < input.size && input[position] == '\t') {
			position++
		}
		return PzlToken(TAB, start span position)
	}
}