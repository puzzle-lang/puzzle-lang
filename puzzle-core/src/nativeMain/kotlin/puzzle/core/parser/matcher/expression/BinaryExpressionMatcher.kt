package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.BinaryExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parseBinaryExpression
import puzzle.core.token.kinds.OperatorKind.*

object BinaryExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<BinaryExpression> {
	
	private val operators = setOf(
		PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,
		EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, TRIPLE_EQUALS, TRIPLE_NOT_EQUALS,
		BIT_AND, BIT_OR, BIT_XOR,
		SHL, SHR, USHR,
		CONTAINS, NOT_CONTAINS,
		AND, OR
	)
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'${cursor.previous.value}' 前未解析到表达式", cursor.previous)
	}
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match { it in operators }
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): BinaryExpression {
		return parseBinaryExpression(left)
	}
}