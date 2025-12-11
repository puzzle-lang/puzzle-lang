package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.parser.ast.TokenRange
import puzzle.core.token.PzlToken
import puzzle.core.token.WhiteSpaceKind

object TabRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != '\t') return null
		var position = start + 1
		while (position < input.size && input[position] == '\t') {
			position++
		}
		return PzlToken(WhiteSpaceKind.TAB, TokenRange(start, position))
	}
}