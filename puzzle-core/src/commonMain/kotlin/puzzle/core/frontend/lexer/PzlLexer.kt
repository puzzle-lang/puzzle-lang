package puzzle.core.frontend.lexer

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.lexer.recognition.*
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACE
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.CommentKind.MultiLine
import puzzle.core.frontend.token.kinds.CommentKind.SingleLine
import puzzle.core.frontend.token.kinds.MetaKind.EOF
import puzzle.core.frontend.token.kinds.WhiteSpaceKind

class PzlLexer(
	private val input: CharArray,
	private var position: Int,
	private val isTemplateExpression: Boolean,
) {
	
	companion object {
		
		fun default(input: CharArray): PzlLexer {
			return PzlLexer(input, 0, false)
		}
		
		fun templateExpression(input: CharArray, position: Int): PzlLexer {
			return PzlLexer(input, position, true)
		}
	}
	
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
	fun scan(): List<PzlToken> {
		return buildList {
			if (isTemplateExpression) {
				var depth = 1
				while (true) {
					val token = nextToken()
					if (token.kind == LBRACE) {
						depth++
					} else if (token.kind == RBRACE && --depth == 0) {
						this += PzlToken(EOF, token.location)
						break
					}
					this += token
					if (token.kind == EOF) break
				}
			} else {
				while (true) {
					val token = nextToken()
					this += token
					if (token.kind == EOF) break
				}
			}
		}
	}
	
	context(_: PzlContext)
	private fun nextToken(): PzlToken {
		recognitions.forEach {
			val token = it.tryParse(input, position) ?: return@forEach
			position = token.location.end
			return when (token.kind) {
				is WhiteSpaceKind, is SingleLine, is MultiLine -> nextToken()
				else -> token
			}
		}
		syntaxError("${input[position]} 无法被识别", position)
	}
}