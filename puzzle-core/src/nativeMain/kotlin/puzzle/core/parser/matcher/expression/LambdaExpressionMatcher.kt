package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.LambdaExpression
import puzzle.core.parser.parser.expression.matchLabel
import puzzle.core.parser.parser.expression.parseLambdaExpression
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

object LambdaExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<LambdaExpression> {
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(LBRACE) || cursor.matchLabel(LBRACE)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("lambda 语句前不允许有表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): LambdaExpression {
		return parseLambdaExpression()
	}
}