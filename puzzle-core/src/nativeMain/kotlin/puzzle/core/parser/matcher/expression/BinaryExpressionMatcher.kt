package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.BinaryExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parseBinaryExpression
import puzzle.core.token.OperatorKind
import puzzle.core.token.OperatorKind.*

object BinaryExpressionMatcher : ExpressionMatcher<BinaryExpression> {
	
	private val operators = arrayOf<OperatorKind>(
		PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,
		EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, TRIPLE_EQUALS, TRIPLE_NOT_EQUALS,
		BIT_AND, BIT_OR, BIT_XOR,
		SHL, SHR, USHR,
		CONTAINS, NOT_CONTAINS,
		AND, OR
	)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return operators.any { cursor.match(it) }
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): BinaryExpression {
		if (left == null) {
			syntaxError("二元运算符未解析到左值", cursor.previous)
		}
		return parseBinaryExpression(left)
	}
}