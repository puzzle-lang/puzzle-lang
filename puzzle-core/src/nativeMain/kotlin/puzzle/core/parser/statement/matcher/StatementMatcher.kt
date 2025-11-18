package puzzle.core.parser.statement.matcher

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.statement.Statement

sealed interface StatementMatcher<S : Statement> {
	
	fun match(ctx: PzlParserContext): Boolean
	
	context(_: PzlContext)
	fun parse(ctx: PzlParserContext): S
}

private val matchers = listOf(
	VariableDeclarationStatementMatcher
)

context(_: PzlContext)
fun parseStatement(ctx: PzlParserContext): Statement {
	val matcher = matchers.find { it.match(ctx) }
		?: run {
			if (ctx.current.type == PzlTokenType.EOF) {
				syntaxError("函数缺少 '}'", ctx.current)
			} else {
				syntaxError("不支持的语句", ctx.current)
			}
		}
	return matcher.parse(ctx)
}