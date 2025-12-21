package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.GroupingExpression
import puzzle.core.parser.parser.expression.parseGroupingExpression
import puzzle.core.token.kinds.BracketKind.Start.LPAREN

object GroupingExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<GroupingExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(LPAREN)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'(' 前不允许有表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): GroupingExpression {
		return parseGroupingExpression()
	}
}