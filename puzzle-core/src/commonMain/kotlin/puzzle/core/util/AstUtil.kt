package puzzle.core.util

import puzzle.core.parser.ast.Modifier
import puzzle.core.token.kinds.ModifierKind

infix fun ModifierKind.isIn(modifiers: List<Modifier>): Boolean {
	return modifiers.any { it.kind == this }
}