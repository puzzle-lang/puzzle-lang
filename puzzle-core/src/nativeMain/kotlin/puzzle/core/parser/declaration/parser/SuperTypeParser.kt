package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.declaration.SuperClass
import puzzle.core.parser.declaration.SuperTrait
import puzzle.core.parser.declaration.SuperType
import puzzle.core.parser.expression.InvokeType
import puzzle.core.parser.expression.parser.parseArguments
import puzzle.core.parser.node.parser.TypeReferenceParser

context(_: PzlContext)
fun parseSuperTypes(ctx: PzlParserContext): List<SuperType> {
	if (!ctx.match(PzlTokenType.COLON)) {
		return emptyList()
	}
	val superTypes = mutableListOf<SuperType>()
	do {
		superTypes += SuperTypeParser(ctx).parse(hasSuperClass = superTypes.any { it is SuperClass })
	} while (ctx.match(PzlTokenType.COMMA))
	return superTypes
}

private class SuperTypeParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(hasSuperClass: Boolean): SuperType {
		val type = TypeReferenceParser(ctx).parse(isSupportedLambdaType = false)
		if (!ctx.match(PzlTokenType.LPAREN)) {
			return SuperTrait(type)
		}
		if (hasSuperClass) {
			syntaxError("不支持继承多个类", ctx.previous)
		}
		val arguments = parseArguments(ctx, InvokeType.CALL)
		return SuperClass(type, arguments)
	}
}