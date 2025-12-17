package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.token.SourceLocation
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseUniqueDeclaration(
	header: DeclarationHeader,
	start: SourceLocation,
	isMember: Boolean,
): UniqueDeclaration {
	val name = parseIdentifierExpression(
		target = if (isMember) IdentifierTarget.MEMBER_UNIQUE else IdentifierTarget.UNIQUE
	)
	val members = if (cursor.match(BracketKind.Start.LBRACE)) {
		buildList {
			while (!cursor.match(BracketKind.End.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	val end = cursor.previous.location
	return UniqueDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members,
		location = start span end
	)
}