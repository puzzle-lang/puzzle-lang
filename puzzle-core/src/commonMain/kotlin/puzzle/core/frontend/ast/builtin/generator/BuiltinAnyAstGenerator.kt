package puzzle.core.frontend.ast.builtin.generator

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.builtin.buildBuiltinAst
import puzzle.core.frontend.token.kinds.ModifierKind.BUILTIN

fun generateBuiltinAnyAst(): AstFile = buildBuiltinAst("Any.pzl") {
	appendTrait("Any") {
		appendFun(
			name = "==",
			parameters = listOf(parameter("other", "Any", isNullable = true)),
			returnType = "Boolean",
			modifiers = listOf(BUILTIN)
		)
		appendFun(
			name = "hash",
			returnType = "Int",
			modifiers = listOf(BUILTIN)
		)
		appendFun(
			name = "toString",
			returnType = "String",
			modifiers = listOf(BUILTIN)
		)
	}
}