package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.declaration.SuperClass
import puzzle.core.parser.declaration.SuperTrait
import puzzle.core.parser.declaration.SuperType
import puzzle.core.parser.expression.InvokeType
import puzzle.core.parser.expression.parser.parseArguments
import puzzle.core.parser.node.NamedType
import puzzle.core.parser.node.parser.TypeReferenceParser

context(_: PzlContext)
fun parseSuperTypes(
	cursor: PzlTokenCursor,
	isSupportedClass: Boolean = true
): List<SuperType> {
	if (!cursor.match(PzlTokenType.COLON)) {
		return emptyList()
	}
	val superTypes = mutableListOf<SuperType>()
	do {
		superTypes += SuperTypeParser(cursor).parse(
			isSupportedClass = isSupportedClass,
			hasSuperClass = superTypes.any { it is SuperClass }
		)
	} while (cursor.match(PzlTokenType.COMMA))
	return superTypes
}

private class SuperTypeParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(
		isSupportedClass: Boolean,
		hasSuperClass: Boolean
	): SuperType {
		val type = TypeReferenceParser(cursor).parse(isSupportedLambdaType = false)
		if (!cursor.match(PzlTokenType.LPAREN)) {
			return SuperTrait(type)
		}
		val offset = -1 - ((type.type as NamedType).segments.size - 1) * 2
		if (!isSupportedClass) {
			syntaxError("不支持继承类", cursor.offset(offset))
		}
		if (hasSuperClass) {
			syntaxError("不支持继承多个类", cursor.offset(offset))
		}
		val arguments = parseArguments(cursor, InvokeType.CALL)
		return SuperClass(type, arguments)
	}
}