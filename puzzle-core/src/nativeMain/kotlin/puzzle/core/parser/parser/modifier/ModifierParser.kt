package puzzle.core.parser.parser.modifier

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.symbol.Modifier
import puzzle.core.symbol.Modifier.*

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseModifiers(): List<Modifier> {
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
private fun parseModifier(): Modifier? {
    return when {
        cursor.match(PzlTokenType.PRIVATE) -> PRIVATE
        cursor.match(PzlTokenType.PROTECTED) -> PROTECTED
        cursor.match(PzlTokenType.FILE) -> FILE
        cursor.match(PzlTokenType.INTERNAL) -> INTERNAL
        cursor.match(PzlTokenType.MODULE) -> MODULE
        cursor.match(PzlTokenType.PUBLIC) -> PUBLIC
        cursor.match(PzlTokenType.FINAL) -> FINAL
        cursor.match(PzlTokenType.OPEN) -> OPEN
        cursor.match(PzlTokenType.ABSTRACT) -> ABSTRACT
        cursor.match(PzlTokenType.SEALED) -> SEALED
        cursor.match(PzlTokenType.OVERRIDE) -> OVERRIDE
        cursor.match(PzlTokenType.CONST) -> CONST
        cursor.match(PzlTokenType.OWNER) -> OWNER
        cursor.match(PzlTokenType.IGNORE) -> IGNORE
        cursor.match(PzlTokenType.LATE) -> LATE
        cursor.match(PzlTokenType.LAZY) -> LAZY
        cursor.match(PzlTokenType.ARGS) -> ARGS
        cursor.match(PzlTokenType.VAR) -> VAR
        cursor.match(PzlTokenType.VAL) -> VAL
        else -> null
    }
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun checkModifierOrder(modifiers: List<Modifier>) {
    if (modifiers.isEmpty()) return
    var last: Modifier? = null
    var lastOrder = -1
    modifiers.forEachIndexed { index, current ->
        when {
            current.order > lastOrder -> {
                lastOrder = current.order
                last = current
            }

            current.order < lastOrder -> syntaxError(
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

context(_: PzlContext, cursor: PzlTokenCursor)
fun List<Modifier>.check(target: ModifierTarget) {
    this.forEachIndexed { index, modifier ->
        if (modifier !in target.supportedModifiers) {
            syntaxError(
                message = "${target.displayName}不支持 '${modifier.name.lowercase()}' 修饰符",
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