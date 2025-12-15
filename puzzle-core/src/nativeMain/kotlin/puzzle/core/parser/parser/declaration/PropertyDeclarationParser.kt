package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.AccessKind
import puzzle.core.token.AssignmentKind
import puzzle.core.token.SymbolKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePropertyDeclaration(header: DeclarationHeader): PropertyDeclaration {
	val (extension, name) = parseExtensionAndPropertyName()
	val type = if (cursor.match(SymbolKind.COLON)) parseTypeReference(isSupportedLambdaType = true) else null
	val initialize = if (cursor.match(AssignmentKind.ASSIGN)) parseExpressionChain() else null
	return PropertyDeclaration(
		name = name,
		type = type,
		modifiers = header.modifiers,
		extension = extension,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		initializer = initialize
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExtensionAndPropertyName(): Pair<TypeReference?, String> {
	val name = parseIdentifierName(IdentifierNameTarget.PROPERTY)
	return if (cursor.check(AccessKind.DOT)) {
		cursor.retreat()
		val type = parseTypeReference()
		if (type.isNullable) {
			cursor.expect(AccessKind.DOT, "属性缺少 '.'")
			val name = parseIdentifierName(IdentifierNameTarget.FUN)
			type to name
		} else {
			val segments = (type.type as NamedType).qualifiedName.toMutableList()
			val name = segments.removeLast()
			val type = TypeReference(NamedType(segments))
			type to name
		}
	} else {
		null to name
	}
}