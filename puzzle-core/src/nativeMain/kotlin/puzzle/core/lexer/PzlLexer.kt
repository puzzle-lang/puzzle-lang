package puzzle.core.lexer

import puzzle.core.model.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.lexer.recognizer.*

class PzlLexer(
	private val input: CharArray
) {
	
	private var position = 0
	
	private val recognizers = arrayOf(
		EOFRecognizer,
		WhiteSpaceRecognizer,
		TabRecognizer,
		NewlineRecognizer,
		CommentRecognizer,
		KeywordRecognizer,
		CharRecognizer,
		StringRecognizer,
		NumberRecognizer,
		SymbolRecognizer,
		IdentifierRecognizer,
	)
	
	context(_: PzlContext)
	fun lex(): List<PzlToken> {
		return buildList {
			while (true) {
				val token = nextToken()
				this += token
				if (token.type == EOF) break
			}
		}
	}
	
	context(_: PzlContext)
	private fun nextToken(): PzlToken {
		recognizers.forEach {
			val token = it.tryParse(input, position) ?: return@forEach
			position = token.range.end
			return when (token.type) {
				NEWLINE, WHITE_SPACE, TAB, SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT -> nextToken()
				else -> token
			}
		}
		syntaxError("${input[position]} 无法被识别", position)
	}
}