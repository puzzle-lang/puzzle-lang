package puzzle.core.parser.matcher.expression

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.BinaryExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.BinaryExpressionParser

object BinaryExpressionMatcher : ExpressionMatcher<BinaryExpression> {
	
	private val tokenTypes = setOf(
		PzlTokenType.PLUS,
		PzlTokenType.MINUS,
		PzlTokenType.STAR,
		PzlTokenType.SLASH,
		PzlTokenType.PERCENT,
		PzlTokenType.DOUBLE_STAR,
		PzlTokenType.EQUALS,
		PzlTokenType.NOT_EQUALS,
		PzlTokenType.GT,
		PzlTokenType.GT_EQUALS,
		PzlTokenType.LT,
		PzlTokenType.LT_EQUALS,
		PzlTokenType.TRIPLE_EQUALS,
		PzlTokenType.BIT_AND,
		PzlTokenType.BIT_OR,
		PzlTokenType.BIT_XOR,
		PzlTokenType.SHL,
		PzlTokenType.SHR,
		PzlTokenType.USHR,
		PzlTokenType.CONTAINS,
		PzlTokenType.NOT_CONTAINS,
		PzlTokenType.AND,
		PzlTokenType.OR
	)
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		return tokenTypes.any { cursor.match(it) }
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): BinaryExpression {
		if (left == null) {
			syntaxError("二元运算符未解析到左值", cursor.previous)
		}
		return BinaryExpressionParser.of(cursor).parse(left)
	}
}