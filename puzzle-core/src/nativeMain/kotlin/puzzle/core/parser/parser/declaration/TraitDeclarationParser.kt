package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.token.BracketKind
import puzzle.core.token.ModifierKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTraitDeclaration(
	typeSpec: TypeSpec?,
	contextSpec: ContextSpec?,
	modifiers: List<ModifierKind>,
	annotationCalls: List<AnnotationCall>,
): TraitDeclaration {
	val name = parseIdentifierName(IdentifierNameTarget.TRAIT)
	val members = if (cursor.match(BracketKind.Start.LBRACE)) {
		buildList {
			while (!cursor.match(BracketKind.End.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	return TraitDeclaration(
		name = name,
		modifiers = modifiers,
		typeSpec = typeSpec,
		contextSpec = contextSpec,
		annotationCalls = annotationCalls,
		members = members
	)
}