package puzzle.core.parser.parser.modifier

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Modifier
import puzzle.core.token.kinds.ModifierKind
import puzzle.core.token.kinds.ModifierKind.*

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseModifiers(): List<Modifier> {
	val modifiers = buildList {
		while (true) {
			val modifier = parseModifier() ?: break
			val kind = modifier.kind
			if (kind == VAR || kind == VAR) break
			this += modifier
		}
	}
	checkModifierOrder(modifiers)
	return modifiers
}

context(cursor: PzlTokenCursor)
private fun parseModifier(): Modifier? {
	ModifierKind.kinds.fastForEach { kind ->
		if (cursor.match(kind)) {
			return Modifier(kind, cursor.previous.location)
		}
	}
	return null
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun checkModifierOrder(modifiers: List<Modifier>) {
	if (modifiers.isEmpty()) return
	var last: Modifier? = null
	var lastOrder = -1
	modifiers.forEachIndexed { index, current ->
		when {
			current.kind.order > lastOrder -> {
				lastOrder = current.kind.order
				last = current
			}
			
			current.kind.order < lastOrder -> syntaxError(
				message = "不规范的修饰符顺序，'${current.kind.value}' 需要在 '${last!!.kind.value}' 前面",
				token = cursor.offset(offset = -modifiers.size + index)
			)
			
			else -> syntaxError(
				message = "'${last!!.kind.value}' 和 '${current.kind.value}' 修饰符不允许同时使用",
				token = cursor.offset(offset = -modifiers.size + index)
			)
		}
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun List<Modifier>.check(target: ModifierTarget) {
	this.forEachIndexed { index, modifier ->
		if (modifier.kind !in target.supportedModifiers) {
			syntaxError(
				message = "${target.displayName}不支持 '${modifier.kind.value}' 修饰符",
				token = cursor.offset(offset = -this.size + index - 1)
			)
		}
	}
	if (this.all { it.kind != FINAL }) return
	if (this.all { it.kind != OVERRIDE }) {
		syntaxError(
			"‘final’ 修饰符必须配合 'override' 修饰符使用",
			cursor.offset(offset = -this.size + this.indexOfFirst { it.kind == FINAL } - 1)
		)
	}
}