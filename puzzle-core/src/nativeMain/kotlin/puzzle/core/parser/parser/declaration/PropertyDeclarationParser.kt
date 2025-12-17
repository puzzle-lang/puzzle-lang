package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.SourceLocation
import puzzle.core.token.copy
import puzzle.core.token.kinds.AccessKind
import puzzle.core.token.kinds.AssignmentKind
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.token.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePropertyDeclaration(header: DeclarationHeader, start: SourceLocation): PropertyDeclaration {
	val (extension, name) = parseExtensionAndPropertyName()
	val type = if (cursor.match(SymbolKind.COLON)) parseTypeReference(isSupportedLambdaType = true) else null
	val initialize = if (cursor.match(AssignmentKind.ASSIGN)) parseExpressionChain() else null
	val end = cursor.previous.location
	return PropertyDeclaration(
		name = name,
		type = type,
		modifiers = header.modifiers,
		extension = extension,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		initializer = initialize,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExtensionAndPropertyName(): Pair<TypeReference?, IdentifierExpression> {
	val name = parseIdentifierExpression(IdentifierTarget.PROPERTY)
	return if (cursor.check(AccessKind.DOT)) {
		cursor.retreat()
		val type = parseTypeReference()
		if (type.isNullable) {
			cursor.expect(AccessKind.DOT, "属性缺少 '.'")
			val name = parseIdentifierExpression(IdentifierTarget.FUN)
			type to name
		} else {
			val segments = (type.type as NamedType).segments.toMutableList()
			val name = IdentifierExpression(
				name = segments.removeLast(),
				location = type.type.location.copy(start = { it.end - 1 })
			)
			val location = type.location.copy(end = { it.end - 2 })
			val type = TypeReference(NamedType(segments, location), location)
			type to name
		}
	} else {
		null to name
	}
}