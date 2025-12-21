package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.parser.parser.parameter.parameter.parseParameters
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationDeclaration(header: DeclarationHeader, start: SourceLocation): AnnotationDeclaration {
	val name = parseIdentifier(IdentifierTarget.ANNOTATION)
	val parameters = parseParameters(ParameterTarget.ANNOTATION)
	if (cursor.match(LBRACE)) {
		syntaxError("注解不支持 '{'", cursor.previous)
	}
	val end = cursor.previous.location
	return AnnotationDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		parameters = parameters,
		typeSpec = header.typeSpec,
		annotationCalls = header.annotationCalls,
		location = start span end
	)
}