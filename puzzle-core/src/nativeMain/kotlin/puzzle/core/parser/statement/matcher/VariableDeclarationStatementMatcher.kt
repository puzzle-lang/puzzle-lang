package puzzle.core.parser.statement.matcher

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.statement.VariableDeclarationStatement
import puzzle.core.parser.statement.parser.VariableDeclarationStatementParser

object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.VAR) || ctx.match(PzlTokenType.VAL)
	}
	
	context(_: PzlContext)
	override fun parse(ctx: PzlParserContext): VariableDeclarationStatement {
		return VariableDeclarationStatementParser(ctx).parse()
	}
}