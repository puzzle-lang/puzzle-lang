package puzzle.core.parser.parser.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.lexer.PzlTokenType.DOT
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.matcher.expression.parseExpressionChain
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePropertyDeclaration(
	typeSpec: TypeSpec?,
	contextSpec: ContextSpec?,
	modifiers: List<Modifier>,
	annotationCalls: List<AnnotationCall>,
): PropertyDeclaration {
	val (extension, name) = parseExtensionAndPropertyName()
	val type = if (cursor.match(PzlTokenType.COLON)) parseTypeReference(isSupportedLambdaType = true) else null
	val initialize = if (cursor.match(PzlTokenType.ASSIGN)) parseExpressionChain() else null
	return PropertyDeclaration(
		name = name,
		type = type,
		modifiers = modifiers,
		extension = extension,
		typeSpec = typeSpec,
		contextSpec = contextSpec,
		annotationCalls = annotationCalls,
		initializer = initialize
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExtensionAndPropertyName(): Pair<TypeReference?, String> {
	val name = parseIdentifierName(IdentifierNameTarget.PROPERTY)
	return if (cursor.check(DOT)) {
		cursor.retreat()
		val type = parseTypeReference()
		if (type.isNullable) {
			cursor.expect(DOT, "属性缺少 '.'")
			val name = parseIdentifierName(IdentifierNameTarget.FUN)
			type to name
		} else {
			val segments = (type.type as NamedType).segments.toMutableList()
			val name = segments.removeLast()
			val type = TypeReference(NamedType(segments))
			type to name
		}
	} else {
		null to name
	}
}