package puzzle.core.parser

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.MetaKind
import puzzle.core.token.kinds.PzlTokenKind
import kotlin.reflect.KClass

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
	
	fun advance(): PzlToken {
		if (position < tokens.size - 1) {
			return current.also { position++ }
		}
		error("position 超出 tokens 的范围")
	}
	
	fun retreat(): PzlToken {
		if (position > 0) {
			return current.also { position-- }
		}
		error("position 超出 tokens 的范围")
	}
	
	inline fun <reified K : PzlTokenKind> match(): Boolean = match(K::class)
	
	fun match(clazz: KClass<out PzlTokenKind>): Boolean {
		if (!check(clazz)) return false
		position++
		return true
	}
	
	fun match(kind: PzlTokenKind): Boolean {
		if (!check(kind)) return false
		position++
		return true
	}
	
	fun match(vararg classes: KClass<out PzlTokenKind>): Boolean {
		if (classes.isEmpty()) {
			error("classes can't be empty.")
		}
		classes.forEachIndexed { index, clazz ->
			val token = offsetOrNull(offset = index) ?: return false
			if (token.kind::class != clazz::class) {
				return false
			}
		}
		position += classes.size
		return true
	}
	
	fun match(vararg kinds: PzlTokenKind): Boolean {
		if (kinds.isEmpty()) {
			error("classes can't be empty.")
		}
		kinds.forEachIndexed { index, kind ->
			val token = offsetOrNull(offset = index) ?: return false
			if (token.kind != kind) {
				return false
			}
		}
		position += kinds.size
		return true
	}
	
	fun check(clazz: KClass<out PzlTokenKind>): Boolean {
		return current.kind::class == clazz
	}
	
	fun check(kind: PzlTokenKind): Boolean {
		return current.kind == kind
	}
	
	context(_: PzlContext)
	fun expect(clazz: KClass<out PzlTokenKind>, errorMessage: String) {
		if (check(clazz)) {
			advance()
			return
		}
		syntaxError(errorMessage, current)
	}
	
	context(_: PzlContext)
	fun expect(kind: PzlTokenKind, errorMessage: String) {
		if (check(kind)) {
			advance()
			return
		}
		syntaxError(errorMessage, current)
	}
	
	fun isAtEnd(): Boolean {
		return position >= tokens.size || current.kind == MetaKind.EOF
	}
	
	operator fun get(index: Int): PzlToken {
		return tokens[index]
	}
}