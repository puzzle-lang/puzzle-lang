package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.access
import puzzle.core.parser.declaration.StructDeclaration
import puzzle.core.parser.parameter.parser.parseStructParameters

class StructDeclarationParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): StructDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "结构体缺少名称")
		val name = cursor.previous.value
		val structAccess = modifiers.access
		val parameters = parseStructParameters(cursor, structAccess)
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