package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.ast.declaration.SuperTrait
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.BracketKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExtensionDeclaration(header: DeclarationHeader): ExtensionDeclaration {
	val extendedType = parseTypeReference()
	val superTraits = parseSuperTypes(isSupportedClass = false)
		.filterIsInstance<SuperTrait>()
	val members = if (cursor.match(BracketKind.Start.LBRACE)) {
		buildList {
			while (!cursor.match(BracketKind.End.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	return ExtensionDeclaration(
		extendedType = extendedType,
		modifiers = header.modifiers,
		superTraits = superTraits,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members,
	)
}