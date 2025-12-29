package puzzle.core.util

import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.token.kinds.ModifierKind

infix fun ModifierKind.isIn(modifiers: List<Modifier>): Boolean {
	return modifiers.any { it.kind == this }
}

val Identifier.isAnonymousBinding: Boolean
	get() = this.name == "_"