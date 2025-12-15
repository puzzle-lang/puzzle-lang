package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.token.BracketKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseUniqueDeclaration(
	header: DeclarationHeader,
	isMember: Boolean
): UniqueDeclaration {
	val name = if (isMember) {
		parseIdentifierName(IdentifierNameTarget.MEMBER_UNIQUE)
	} else {
		parseIdentifierName(IdentifierNameTarget.UNIQUE)
	}
	val members = if (cursor.match(BracketKind.Start.LBRACE)) {
		buildList {
			while (!cursor.match(BracketKind.End.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	return UniqueDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members
	)
}