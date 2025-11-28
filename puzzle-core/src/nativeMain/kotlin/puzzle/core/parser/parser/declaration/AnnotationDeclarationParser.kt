package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.binding.parameter.parseAnnotationParameters
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.symbol.Modifier

class AnnotationDeclarationParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<AnnotationDeclarationParser>(::AnnotationDeclarationParser)
	
	context(_: PzlContext)
	fun parse(
		typeSpec: TypeSpec?,
		modifiers: List<Modifier>
	): AnnotationDeclaration {
		val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.ANNOTATION)
		val parameters = parseAnnotationParameters(cursor)
		if (cursor.match(PzlTokenType.LBRACE)) {
			syntaxError("注解不支持 '{'", cursor.previous)
		}
		return AnnotationDeclaration(
			name = name,
			modifiers = modifiers,
			parameters = parameters,
			typeSpec = typeSpec,
		)
	}
}