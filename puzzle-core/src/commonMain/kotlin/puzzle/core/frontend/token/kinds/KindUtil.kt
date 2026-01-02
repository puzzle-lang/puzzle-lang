package puzzle.core.frontend.token.kinds

import puzzle.core.frontend.parser.ast.Modifier

infix fun ModifierKind.isIn(modifiers: List<Modifier>): Boolean {
	return modifiers.any { it.kind == this }
}