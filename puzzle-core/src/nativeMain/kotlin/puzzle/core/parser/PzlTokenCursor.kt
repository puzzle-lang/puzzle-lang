package puzzle.core.parser

import puzzle.core.model.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType

class PzlTokenCursor(
	private val tokens: List<PzlToken>
) {
	
	var position = 0
		private set
	
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
	
	fun retreat(count: Int = 1): PzlToken {
		if (position >= count) {
			position -= count
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
		syntaxError(errorMessage, position)
	}
	
	fun isAtEnd(): Boolean {
		return position >= tokens.size || current.type == PzlTokenType.EOF
	}
	
	operator fun get(index: Int): PzlToken {
		return tokens[index]
	}
}