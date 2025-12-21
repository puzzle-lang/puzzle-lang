package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.NonNullAssertionExpression
import puzzle.core.parser.parser.expression.parseNonNullAssertionExpression
import puzzle.core.token.kinds.OperatorKind.NOT

object NonNullAssertionExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<NonNullAssertionExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		if (left == null) return false
		return cursor.match(NOT)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'!' 断言前未匹配到表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): NonNullAssertionExpression {
		return parseNonNullAssertionExpression(left)
	}
}