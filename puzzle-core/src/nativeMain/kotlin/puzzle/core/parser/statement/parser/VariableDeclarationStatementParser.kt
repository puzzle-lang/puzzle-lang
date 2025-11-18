package puzzle.core.parser.statement.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.matcher.parseCompleteExpression
import puzzle.core.parser.node.TypeReferenceParser
import puzzle.core.parser.statement.VariableDeclarationStatement

class VariableDeclarationStatementParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(): VariableDeclarationStatement {
		val isVariable = ctx.previous.type == PzlTokenType.VAR
		ctx.expect(PzlTokenType.IDENTIFIER, "变量缺少名称")
		val name = ctx.previous.value
		val type = if (ctx.match(PzlTokenType.COLON)) TypeReferenceParser(ctx).parse() else null
		val initializer = if (ctx.match(PzlTokenType.ASSIGN)) {
			parseCompleteExpression(ctx)
		} else null
		return VariableDeclarationStatement(
			name = name,
			initializer = initializer,
			isVariable = isVariable,
			type = type
		)
	}
}