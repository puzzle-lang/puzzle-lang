package puzzle.core.parser

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.MetaKind.EOF
import puzzle.core.token.kinds.PzlTokenKind

class PzlTokenCursor(
	private val tokens: List<PzlToken>,
) {
	
	var position = 0
		private set
	
	val current: PzlToken
		get() = this.tokens[position]
	
	val previous: PzlToken
		get() = this.tokens[position - 1]
	
	val next: PzlToken
		get() = this.tokens[position + 1]
	
	val nextOrNull: PzlToken?
		get() = this.tokens.getOrNull(position + 1)
	
	fun offset(offset: Int): PzlToken {
		return tokens[position + offset]
	}
	
	fun offsetOrNull(offset: Int): PzlToken? {
		return tokens.getOrNull(position + offset)
	}
	
	fun advance(count: Int = 1) {
		if (position < tokens.size - count) {
			position += count
		} else {
			error("position 超出 tokens 的范围")
		}
	}
	
	fun retreat(count: Int = 1) {
		if (position > count - 1) {
			position -= count
		} else {
			error("position 超出 tokens 的范围")
		}
	}
	
	inline fun <reified K : PzlTokenKind> matchIsInstance(): Boolean {
		if (current.kind !is K) return false
		advance()
		return true
	}
	
	fun match(kind: PzlTokenKind): Boolean {
		if (current.kind != kind) return false
		position++
		return true
	}
	
	fun match(kind1: PzlTokenKind, kind2: PzlTokenKind, vararg kinds: PzlTokenKind): Boolean {
		val kinds = arrayOf(kind1, kind2) + kinds
		kinds.forEachIndexed { offset, kind ->
			val token = offsetOrNull(offset = offset) ?: return false
			if (token.kind != kind) {
				return false
			}
		}
		position += kinds.size
		return true
	}
	
	fun match(predicate: (PzlTokenKind) -> Boolean): Boolean {
		if (!predicate(current.kind)) return false
		position++
		return true
	}
	
	fun check(kind: PzlTokenKind): Boolean {
		return current.kind == kind
	}
	
	fun check(check: (PzlTokenKind) -> Boolean): Boolean {
		return check(current.kind)
	}
	
	context(_: PzlContext)
	fun expect(kind: PzlTokenKind, errorMessage: String) {
		if (current.kind == kind) {
			position++
			return
		}
		syntaxError(errorMessage, current)
	}
	
	fun isAtEnd(): Boolean {
		return position >= tokens.size || current.kind == EOF
	}
	
	operator fun get(index: Int): PzlToken {
		return tokens[index]
	}
}