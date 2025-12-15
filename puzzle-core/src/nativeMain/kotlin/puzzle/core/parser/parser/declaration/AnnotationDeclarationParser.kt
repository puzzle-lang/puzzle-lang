package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parameter.parameter.parseAnnotationParameters
import puzzle.core.token.BracketKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationDeclaration(header: DeclarationHeader): AnnotationDeclaration {
	val name = parseIdentifierName(IdentifierNameTarget.ANNOTATION)
	val parameters = parseAnnotationParameters()
	if (cursor.match(BracketKind.Start.LBRACE)) {
		syntaxError("注解不支持 '{'", cursor.previous)
	}
	return AnnotationDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		parameters = parameters,
		typeSpec = header.typeSpec,
		annotationCalls = header.annotationCalls
	)
}