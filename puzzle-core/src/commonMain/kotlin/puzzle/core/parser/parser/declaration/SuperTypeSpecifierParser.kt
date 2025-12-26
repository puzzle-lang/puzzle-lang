package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.SuperConstructorCall
import puzzle.core.parser.ast.declaration.SuperTypeReference
import puzzle.core.parser.ast.declaration.SuperTypeSpecifier
import puzzle.core.parser.parser.expression.parseArguments
import puzzle.core.parser.parser.type.parseNamedType
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseSuperTypeSpecifiers(target: SuperTypeSpecifierTarget): List<SuperTypeSpecifier> {
	if (!cursor.match(COLON)) {
		return emptyList()
	}
	val superTypeSpecifiers = mutableListOf<SuperTypeSpecifier>()
	do {
		superTypeSpecifiers += parseSuperTypeSpecifier(target)
	} while (cursor.match(COMMA))
	superTypeSpecifiers.check()
	return superTypeSpecifiers
}

@Suppress("UNCHECKED_CAST")
fun List<SuperTypeSpecifier>.safeAsSuperTypeReferences(): List<SuperTypeReference> {
	return this as List<SuperTypeReference>
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseSuperTypeSpecifier(target: SuperTypeSpecifierTarget): SuperTypeSpecifier {
	val type = parseNamedType()
	if (!cursor.match(LPAREN)) {
		return SuperTypeReference(type, type.location)
	}
	if (!target.allowConstructorCall) {
		syntaxError("${target.label} 不允许使用构造函数调用", type)
	}
	val arguments = parseArguments()
	val end = cursor.previous.location
	return SuperConstructorCall(type, arguments, type.location span end)
}

context(_: PzlContext)
private fun List<SuperTypeSpecifier>.check() {
	var isUsedConstructorCall = false
	this.forEach { it ->
		if (it is SuperConstructorCall) {
			if (isUsedConstructorCall) {
				syntaxError("只允许继承单个类", it)
			}
			isUsedConstructorCall = true
		}
	}
}

enum class SuperTypeSpecifierTarget(
	val label: String,
	val allowConstructorCall: Boolean,
) {
	CLASS("类", allowConstructorCall = true),
	OBJECT("单例对象", allowConstructorCall = true),
	TRAIT("特征", allowConstructorCall = false),
	EXTENSION("扩展", allowConstructorCall = false),
	STRUCT("结构体", allowConstructorCall = false),
	ENUM("结构体", allowConstructorCall = false)
}