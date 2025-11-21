package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.access
import puzzle.core.parser.declaration.AnnotationDeclaration
import puzzle.core.parser.parameter.parser.parseAnnotationParameters

class AnnotationDeclarationParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): AnnotationDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "注解缺少名称")
		val annotationAccess = modifiers.access
		val name = cursor.previous.value
		val parameters = parseAnnotationParameters(cursor, annotationAccess)
		if (cursor.match(PzlTokenType.LBRACE)) {
			syntaxError("注解不支持 '{'", cursor.previous)
		}
		return AnnotationDeclaration(
			name = name,
			modifiers = modifiers,
			parameters = parameters,
		)
	}
}