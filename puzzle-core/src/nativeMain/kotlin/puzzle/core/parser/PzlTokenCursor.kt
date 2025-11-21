package puzzle.core.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType
import puzzle.core.lexer.removeComments

class PzlTokenCursor(
	rawTokens: List<PzlToken>
) {
	
	private val tokens = rawTokens.removeComments()
	
	private var position = 0
	
	val current: PzlToken
		get() = this.tokens[position]
	
	val previous: PzlToken
		get() = this.tokens[position - 1]
	
	val next: PzlToken
		get() = this.tokens[position + 1]
	
	fun offset(offset: Int): PzlToken {
		return tokens[position + offset]
	}
	
	fun offsetOrNull(offset: Int): PzlToken? {
		return tokens.getOrNull(position + offset)
	}
	
	fun advance(): PzlToken {
		if (position < tokens.size - 1) {
			position++
		}
		return current
	}
	
	fun retreat(): PzlToken {
		if (position > 0) {
			position--
		}
		return current
	}
	
	fun match(type: PzlTokenType, vararg types: PzlTokenType): Boolean {
		if (!check(type)) {
			return false
		}
		types.forEachIndexed { index, type ->
			if (offsetOrNull(offset = index + 1)?.type != type) {
				return false
			}
		}
		position += types.size + 1
		return true
	}
	
	fun check(type: PzlTokenType): Boolean {
		return !isAtEnd() && current.type == type
	}
	
	context(_: PzlContext)
	fun expect(type: PzlTokenType, errorMessage: String) {
		if (check(type)) {
			advance()
			return
		}
		syntaxError(errorMessage, current.line, current.column)
	}
	
	fun isAtEnd(): Boolean {
		return position >= tokens.size || current.type == PzlTokenType.EOF
	}
}