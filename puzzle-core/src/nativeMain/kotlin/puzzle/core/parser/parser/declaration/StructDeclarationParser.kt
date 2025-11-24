package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.parser.binding.parameter.parseStructParameters
import puzzle.core.symbol.Modifier

class StructDeclarationParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<StructDeclarationParser>(::StructDeclarationParser)
	
	context(_: PzlContext)
	fun parse(modifiers: List<Modifier>): StructDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "结构体缺少名称")
		val name = cursor.previous.value
		val parameters = parseStructParameters(cursor)
		if (cursor.match(PzlTokenType.LBRACE)) {
			syntaxError("结构体不支持 '{'", cursor.previous)
		}
		return StructDeclaration(
			name = name,
			modifiers = modifiers,
			parameters = parameters
		)
	}
}