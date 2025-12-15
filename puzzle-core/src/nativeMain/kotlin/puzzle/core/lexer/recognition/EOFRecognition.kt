package puzzle.core.lexer.recognition

import puzzle.core.model.PzlContext
import puzzle.core.token.MetaKind
import puzzle.core.token.PzlToken

object EOFRecognition : TokenRecognition {
	
	context(_: PzlContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (start < input.size) return null
		return PzlToken(MetaKind.EOF, start, start + 1)
	}
}