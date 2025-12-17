package puzzle.core.lexer

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.recognition.*
import puzzle.core.model.PzlContext
import puzzle.core.token.kinds.CommentKind
import puzzle.core.token.kinds.MetaKind
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.WhiteSpaceKind

class PzlLexer(
	private val input: CharArray
) {
	
	private var position = 0
	
	private val recognitions = arrayOf(
		EOFRecognition,
		WhiteSpaceRecognition,
		TabRecognition,
		NewlineRecognition,
		CommentRecognition,
		KeywordRecognition,
		CharRecognition,
		StringRecognition,
		NumberRecognition,
		SymbolRecognition,
		IdentifierRecognition,
	)
	
	context(_: PzlContext)
	fun lex(): List<PzlToken> {
		return buildList {
			while (true) {
				val token = nextToken()
				this += token
				if (token.kind == MetaKind.EOF) break
			}
		}
	}
	
	context(_: PzlContext)
	private fun nextToken(): PzlToken {
		recognitions.forEach {
			val token = it.tryParse(input, position) ?: return@forEach
			position = token.location.end
			return when (token.kind) {
				is WhiteSpaceKind, is CommentKind.SingleLine, is CommentKind.MultiLine -> nextToken()
				else -> token
			}
		}
		syntaxError("${input[position]} 无法被识别", position)
	}
}