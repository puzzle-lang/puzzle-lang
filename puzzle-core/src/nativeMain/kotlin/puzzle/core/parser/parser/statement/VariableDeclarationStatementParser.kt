package puzzle.core.parser.parser.statement

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.matcher.expression.parseCompleteExpression
import puzzle.core.parser.parser.node.TypeReferenceParser
import puzzle.core.parser.ast.statement.VariableDeclarationStatement

class VariableDeclarationStatementParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<VariableDeclarationStatementParser>(::VariableDeclarationStatementParser)
	
	context(_: PzlContext)
	fun parse(): VariableDeclarationStatement {
		val isVariable = cursor.previous.type == PzlTokenType.VAR
		cursor.expect(PzlTokenType.IDENTIFIER, "变量缺少名称")
		val name = cursor.previous.value
		val type = if (cursor.match(PzlTokenType.COLON)) {
			TypeReferenceParser.of(cursor).parse(isSupportedLambdaType = true)
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