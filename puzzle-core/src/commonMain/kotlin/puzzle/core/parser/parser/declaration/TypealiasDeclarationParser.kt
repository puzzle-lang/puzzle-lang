package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.TypealiasDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind.ASSIGN

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypealiasDeclaration(header: DeclarationHeader, start: SourceLocation): TypealiasDeclaration {
	val name = parseIdentifier(IdentifierTarget.TYPEALIAS)
	cursor.expect(ASSIGN, "类型别名缺少 '='")
	val targetType = parseTypeReference(allowLambda = true)
	val end = cursor.previous.location
	return TypealiasDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		typeSpec = header.typeSpec,
		targetType = targetType,
		location = start span end
	)
}