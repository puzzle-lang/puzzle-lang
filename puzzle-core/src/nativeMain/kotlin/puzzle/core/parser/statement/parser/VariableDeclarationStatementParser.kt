package puzzle.core.parser.statement.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.matcher.parseCompleteExpression
import puzzle.core.parser.node.parser.TypeReferenceParser
import puzzle.core.parser.statement.VariableDeclarationStatement

class VariableDeclarationStatementParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(): VariableDeclarationStatement {
		val isVariable = cursor.previous.type == PzlTokenType.VAR
		cursor.expect(PzlTokenType.IDENTIFIER, "变量缺少名称")
		val name = cursor.previous.value
		val type = if (cursor.match(PzlTokenType.COLON)) {
			TypeReferenceParser(cursor).parse(isSupportedLambdaType = true)
		} else null
		val initializer = if (cursor.match(PzlTokenType.ASSIGN)) {
			parseCompleteExpression(cursor)
		} else null
		return VariableDeclarationStatement(
			name = name,
			initializer = initializer,
			isVariable = isVariable,
			type = type
		)
	}
}