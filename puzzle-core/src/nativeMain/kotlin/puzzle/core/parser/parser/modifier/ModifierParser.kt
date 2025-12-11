package puzzle.core.parser.parser.modifier

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.token.ModifierKind
import puzzle.core.token.ModifierKind.*

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseModifiers(): List<ModifierKind> {
	val modifiers = buildList {
		while (true) {
			val modifier = parseModifier() ?: break
			if (modifier == VAR || modifier == VAR) break
			this += modifier
		}
	}
	checkModifierOrder(modifiers)
	return modifiers
}

context(cursor: PzlTokenCursor)
private fun parseModifier(): ModifierKind? {
	ModifierKind.kinds.fastForEach { kind ->
		if (cursor.match(kind)) {
			return kind
		}
	}
	return null
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun checkModifierOrder(modifiers: List<ModifierKind>) {
	if (modifiers.isEmpty()) return
	var last: ModifierKind? = null
	var lastOrder = -1
	modifiers.forEachIndexed { index, current ->
		when {
			current.order > lastOrder -> {
				lastOrder = current.order
				last = current
			}
			
			current.order < lastOrder -> syntaxError(
				message = "不规范的修饰符顺序，'${current.value}' 需要在 '${last!!.value}' 前面",
				token = cursor.offset(offset = -modifiers.size + index)
			)
			
			else -> syntaxError(
				message = "'${last!!.value}' 和 '${current.value}' 修饰符不允许同时使用",
				token = cursor.offset(offset = -modifiers.size + index)
			)
		}
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun List<ModifierKind>.check(target: ModifierTarget) {
	this.forEachIndexed { index, modifier ->
		if (modifier !in target.supportedModifiers) {
			syntaxError(
				message = "${target.displayName}不支持 '${modifier.value}' 修饰符",
				token = cursor.offset(offset = -this.size + index - 1)
			)
		}
	}
	if (FINAL !in this) return
	if (OVERRIDE !in this) {
		syntaxError(
			"‘final’ 修饰符必须配合 'override' 修饰符使用",
			cursor.offset(offset = -this.size + this.indexOf(FINAL) - 1)
		)
	}
}