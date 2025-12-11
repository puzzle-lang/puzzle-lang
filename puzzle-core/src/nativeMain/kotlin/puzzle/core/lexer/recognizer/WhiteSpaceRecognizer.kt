package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.parser.ast.TokenRange
import puzzle.core.token.PzlToken
import puzzle.core.token.WhiteSpaceKind

object WhiteSpaceRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != ' ') return null
		var position = start + 1
		while (position < input.size && input[position] == ' ') {
			position++
		}
		return PzlToken(WhiteSpaceKind.WHITESPACE, TokenRange(start, position))
	}
}