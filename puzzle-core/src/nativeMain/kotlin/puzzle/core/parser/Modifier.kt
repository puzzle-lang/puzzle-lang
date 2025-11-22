package puzzle.core.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier.*
import puzzle.core.parser.declaration.NodeKind

enum class Modifier {
	PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
	FINAL, OPEN, ABSTRACT,
	OVERRIDE, CONST, OWNER, IGNORE,
	ARGS,
	VAR, VAL
}

context(_: PzlContext)
fun parseModifiers(cursor: PzlTokenCursor): List<Modifier> {
	val modifiers = mutableListOf<Modifier>()
	while (true) {
		modifiers += when {
			cursor.match(PzlTokenType.PRIVATE) -> PRIVATE
			cursor.match(PzlTokenType.PROTECTED) -> PROTECTED
			cursor.match(PzlTokenType.FILE) -> FILE
			cursor.match(PzlTokenType.INTERNAL) -> INTERNAL
			cursor.match(PzlTokenType.MODULE) -> MODULE
			cursor.match(PzlTokenType.PUBLIC) -> PUBLIC
			cursor.match(PzlTokenType.FINAL) -> FINAL
			cursor.match(PzlTokenType.IGNORE) -> IGNORE
			cursor.match(PzlTokenType.OPEN) -> OPEN
			cursor.match(PzlTokenType.ABSTRACT) -> ABSTRACT
			cursor.match(PzlTokenType.OVERRIDE) -> OVERRIDE
			cursor.match(PzlTokenType.CONST) -> CONST
			cursor.match(PzlTokenType.OWNER) -> OWNER
			cursor.match(PzlTokenType.VAR) -> VAR
			cursor.match(PzlTokenType.VAL) -> VAL
			cursor.match(PzlTokenType.ARGS) -> ARGS
			else -> break
		}
	}
	checkModifierOrder(cursor, modifiers)
	return modifiers
}

private val modifierOrders = mapOf(
	PRIVATE to 0, PROTECTED to 0, FILE to 0, INTERNAL to 0, MODULE to 0, PUBLIC to 0,
	FINAL to 1, OPEN to 1, ABSTRACT to 1,
	OVERRIDE to 2, CONST to 2, OWNER to 2, IGNORE to 2,
	ARGS to 3,
	VAR to 4, VAL to 4
)

context(_: PzlContext)
inline private fun checkModifierOrder(cursor: PzlTokenCursor, modifiers: List<Modifier>) {
	if (modifiers.isEmpty()) return
	var last: Modifier? = null
	var lastOrder = -1
	modifiers.forEachIndexed { index, current ->
		val currentOrder = modifierOrders[current]!!
		when {
			currentOrder > lastOrder -> {
				lastOrder = currentOrder
				last = current
			}
			
			currentOrder < lastOrder -> syntaxError(
				message = "不规范的修饰符顺序，'${current.name.lowercase()}' 需要在 '${last!!.name.lowercase()}' 前面",
				token = cursor.offset(offset = -modifiers.size + index)
			)
			
			else -> syntaxError(
				message = "'${last!!.name.lowercase()}' 和 '${current.name.lowercase()}' 修饰符不允许同时使用",
				token = cursor.offset(offset = -modifiers.size + index)
			)
		}
	}
}

context(_: PzlContext)
fun checkModifiers(
	cursor: PzlTokenCursor,
	modifiers: List<Modifier>,
	nodeKind: NodeKind
) {
	modifiers.forEachIndexed { index, modifier ->
		if (modifier !in nodeKind.supportedModifiers) {
			syntaxError(
				message = "${nodeKind.displayName}不支持 '${modifier.name.lowercase()}' 修饰符",
				token = cursor.offset(offset = -modifiers.size + index - 1)
			)
		}
	}
	if (FINAL !in modifiers) return
	if (OVERRIDE !in modifiers) {
		syntaxError("‘final’ 修饰符必须配合 'override' 修饰符使用", cursor.offset(offset = -modifiers.size + modifiers.indexOf(FINAL) - 1))
	}
}