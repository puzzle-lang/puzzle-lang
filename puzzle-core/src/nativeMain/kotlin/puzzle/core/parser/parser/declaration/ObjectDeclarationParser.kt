package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ObjectDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.token.kinds.BracketKind.End.RBRACE
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseObjectDeclaration(
	header: DeclarationHeader,
	start: SourceLocation,
	isMember: Boolean,
): ObjectDeclaration {
	val name = parseIdentifier(
		target = if (isMember) IdentifierTarget.MEMBER_OBJECT else IdentifierTarget.OBJECT
	)
	val members = if (cursor.match(LBRACE)) {
		buildList {
			while (!cursor.match(RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	val end = cursor.previous.location
	return ObjectDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members,
		location = start span end
	)
}