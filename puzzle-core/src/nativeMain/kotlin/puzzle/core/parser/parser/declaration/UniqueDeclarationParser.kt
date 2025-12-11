package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.token.BracketKind
import puzzle.core.token.ModifierKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseUniqueDeclaration(
	contextSpec: ContextSpec?,
	modifiers: List<ModifierKind>,
	annotationCalls: List<AnnotationCall>,
	isMember: Boolean
): UniqueDeclaration {
	val name = if (isMember) {
		parseIdentifierName(IdentifierNameTarget.MEMBER_UNIQUE)
	} else {
		parseIdentifierName(IdentifierNameTarget.UNIQUE)
	}
	val members = if (cursor.match(BracketKind.LBRACE)) {
		buildList {
			while (!cursor.match(BracketKind.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	return UniqueDeclaration(
		name = name,
		modifiers = modifiers,
		contextSpec = contextSpec,
		annotationCalls = annotationCalls,
		members = members
	)
}