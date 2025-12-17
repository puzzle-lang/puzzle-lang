package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.WhiteSpaceKind
import puzzle.core.model.span

object TabRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != '\t') return null
		var position = start + 1
		while (position < input.size && input[position] == '\t') {
			position++
		}
		return PzlToken(WhiteSpaceKind.TAB, start span position)
	}
}