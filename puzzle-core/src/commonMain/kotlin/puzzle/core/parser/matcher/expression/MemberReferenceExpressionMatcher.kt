package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parseMemberReferenceExpression
import puzzle.core.parser.parser.expression.parsePostfixExpression
import puzzle.core.token.kinds.AccessKind.DOUBLE_COLON

object MemberReferenceExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<Expression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(DOUBLE_COLON)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("语法错误", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): Expression {
		val expression = parseMemberReferenceExpression(receiver = null)
		return parsePostfixExpression(expression)
	}
}