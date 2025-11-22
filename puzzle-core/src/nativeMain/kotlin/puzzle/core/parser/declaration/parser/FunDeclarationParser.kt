package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.constants.PzlTypes
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.binding.parser.parseFunParameters
import puzzle.core.parser.declaration.FunDeclaration
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.node.parser.TypeReferenceParser
import puzzle.core.parser.statement.Statement
import puzzle.core.parser.statement.matcher.parseStatement

class FunDeclarationParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(modifiers: List<Modifier>): FunDeclaration {
		cursor.expect(PzlTokenType.IDENTIFIER, "函数缺少名称")
		val funName = cursor.previous.value
		val parameters = parseFunParameters(cursor)
		
		val returnTypes = mutableListOf<TypeReference>()
		if (cursor.match(PzlTokenType.COLON)) {
			do {
				returnTypes += TypeReferenceParser(cursor).parse()
			} while (cursor.match(PzlTokenType.COMMA))
		} else {
			returnTypes += TypeReference(PzlTypes.Unit)
		}
		
		if (!cursor.match(PzlTokenType.LBRACE)) {
			return FunDeclaration(
				name = funName,
				parameters = parameters,
				modifiers = modifiers,
				returnTypes = returnTypes
			)
		}
		val statements = mutableListOf<Statement>()
		while (!cursor.match(PzlTokenType.RBRACE)) {
			statements += parseStatement(cursor)
		}
		return FunDeclaration(
			name = funName,
			parameters = parameters,
			modifiers = modifiers,
			returnTypes = returnTypes,
			statements = statements
		)
	}
}