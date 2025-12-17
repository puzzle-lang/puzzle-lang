package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.MetaKind
import puzzle.core.model.span

object EOFRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (start < input.size) return null
		return PzlToken(MetaKind.EOF, start span start + 1)
	}
}