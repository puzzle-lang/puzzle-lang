package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.declaration.SuperClass
import puzzle.core.parser.ast.declaration.SuperTrait
import puzzle.core.parser.ast.declaration.SuperType
import puzzle.core.parser.parser.expression.parseArguments
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.SeparatorKind
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.token.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseSuperTypes(
	isSupportedClass: Boolean = true,
): List<SuperType> {
	if (!cursor.match(SymbolKind.COLON)) {
		return emptyList()
	}
	val superTypes = mutableListOf<SuperType>()
	do {
		superTypes += parseSuperType(
			isSupportedClass = isSupportedClass,
			hasSuperClass = superTypes.any { it is SuperClass }
		)
	} while (cursor.match(SeparatorKind.COMMA))
	return superTypes
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseSuperType(
	isSupportedClass: Boolean,
	hasSuperClass: Boolean,
): SuperType {
	val type = parseTypeReference(isSupportedNullable = false)
	if (!cursor.match(BracketKind.Start.LPAREN)) {
		return SuperTrait(type, type.location)
	}
	val offset = -1 - ((type.type as NamedType).segments.size - 1) * 2
	if (!isSupportedClass) {
		syntaxError("不支持继承类", cursor.offset(offset))
	}
	if (hasSuperClass) {
		syntaxError("不支持继承多个类", cursor.offset(offset))
	}
	val arguments = parseArguments(BracketKind.End.RPAREN)
	val location = type.location span cursor.previous.location
	return SuperClass(type, location, arguments)
}