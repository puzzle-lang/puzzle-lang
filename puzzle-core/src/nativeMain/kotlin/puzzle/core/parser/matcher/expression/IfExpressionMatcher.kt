package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IfExpression
import puzzle.core.parser.parser.expression.parseIfExpression
import puzzle.core.token.kinds.ControlFlowKind.IF

object IfExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<IfExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(IF)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("if 前不允许有表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): IfExpression {
		return parseIfExpression()
	}
}