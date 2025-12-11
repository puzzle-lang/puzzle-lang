package puzzle.core.lexer.recognizer

import puzzle.core.model.PzlContext
import puzzle.core.parser.ast.TokenRange
import puzzle.core.token.MetaKind
import puzzle.core.token.PzlToken

object EOFRecognizer : TokenRecognizer {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (start < input.size) return null
		return PzlToken(MetaKind.EOF, TokenRange(start, start + 1))
	}
}