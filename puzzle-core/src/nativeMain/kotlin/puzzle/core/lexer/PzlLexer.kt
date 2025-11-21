package puzzle.core.lexer

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.recognizer.*
import puzzle.core.util.TAB_LENGTH

class PzlLexer(
	private val input: CharArray
) {
	
	private var position = 0
	private var line = 1
	private var column = 1
	
	private val recognizers = listOf(
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
				if (token.type == PzlTokenType.EOF) break
			}
		}
	}
	
	context(_: PzlContext)
	private fun nextToken(): PzlToken {
		recognizers.forEach {
			val token = it.tryParse(input, position, line, column) ?: return@forEach
			position = token.end
			column += if (token.type != PzlTokenType.TAB) token.length else token.length * TAB_LENGTH
			if (token.line != token.endLine) {
				line = token.endLine
			}
			return when (token.type) {
				PzlTokenType.NEWLINE -> doNewline()
				PzlTokenType.WHITE_SPACE, PzlTokenType.TAB -> nextToken()
				else -> token
			}
		}
		syntaxError("${input[position]} 无法被识别", line, column)
	}
	
	context(_: PzlContext)
	private fun doNewline(): PzlToken {
		column = 1
		line++
		return nextToken()
	}
}