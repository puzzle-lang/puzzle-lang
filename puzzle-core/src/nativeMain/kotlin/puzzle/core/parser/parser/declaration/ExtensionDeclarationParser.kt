package puzzle.core.parser.parser.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.ast.declaration.SuperTrait
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.matcher.declaration.parseMemberDeclaration
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExtensionDeclaration(
	typeSpec: TypeSpec?,
	contextSpec: ContextSpec?,
	modifiers: List<Modifier>,
	annotationCalls: List<AnnotationCall>,
): ExtensionDeclaration {
	val extendedType = parseTypeReference()
	val superTraits = parseSuperTypes(isSupportedClass = false)
		.filterIsInstance<SuperTrait>()
	val members = if (cursor.match(PzlTokenType.LBRACE)) {
		buildList {
			while (!cursor.match(PzlTokenType.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	return ExtensionDeclaration(
		extendedType = extendedType,
		modifiers = modifiers,
		superTraits = superTraits,
		typeSpec = typeSpec,
		contextSpec = contextSpec,
		annotationCalls = annotationCalls,
		members = members,
	)
}