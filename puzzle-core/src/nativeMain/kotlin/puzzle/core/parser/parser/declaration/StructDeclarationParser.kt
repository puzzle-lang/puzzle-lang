package puzzle.core.parser.parser.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parameter.parameter.parseStructParameters
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStructDeclaration(
	typeSpec: TypeSpec?,
	contextSpec: ContextSpec?,
	modifiers: List<Modifier>,
	annotationCalls: List<AnnotationCall>,
): StructDeclaration {
	val name = parseIdentifierName(IdentifierNameTarget.STRUCT)
	val parameters = parseStructParameters()
	val members = if (cursor.match(PzlTokenType.LBRACE)) {
		buildList {
			while (!cursor.match(PzlTokenType.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	return StructDeclaration(
		name = name,
		modifiers = modifiers,
		parameters = parameters,
		typeSpec = typeSpec,
		contextSpec = contextSpec,
		annotationCalls = annotationCalls,
		members = members
	)
}