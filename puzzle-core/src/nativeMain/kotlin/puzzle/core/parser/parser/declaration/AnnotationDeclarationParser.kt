package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parameter.parameter.parseAnnotationParameters
import puzzle.core.token.BracketKind
import puzzle.core.token.ModifierKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationDeclaration(
	typeSpec: TypeSpec?,
	modifiers: List<ModifierKind>,
	annotationCalls: List<AnnotationCall>,
): AnnotationDeclaration {
	val name = parseIdentifierName(IdentifierNameTarget.ANNOTATION)
	val parameters = parseAnnotationParameters()
	if (cursor.match(BracketKind.Start.LBRACE)) {
		syntaxError("注解不支持 '{'", cursor.previous)
	}
	return AnnotationDeclaration(
		name = name,
		modifiers = modifiers,
		parameters = parameters,
		typeSpec = typeSpec,
		annotationCalls = annotationCalls
	)
}