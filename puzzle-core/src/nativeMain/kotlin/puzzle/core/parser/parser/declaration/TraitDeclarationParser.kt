package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTraitDeclaration(header: DeclarationHeader, start: SourceLocation): TraitDeclaration {
	val name = parseIdentifier(IdentifierTarget.TRAIT)
	val members = if (cursor.match(LBRACE)) parseMemberDeclarations() else emptyList()
	val end = cursor.previous.location
	return TraitDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members,
		location = start span end
	)
}