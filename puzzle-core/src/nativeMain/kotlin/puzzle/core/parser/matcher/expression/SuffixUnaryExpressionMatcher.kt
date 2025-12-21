package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.parser.parser.expression.isIdentifier
import puzzle.core.parser.parser.expression.parseSuffixUnaryExpression
import puzzle.core.token.kinds.OperatorKind.DOUBLE_MINUS
import puzzle.core.token.kinds.OperatorKind.DOUBLE_PLUS

object SuffixUnaryExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<SuffixUnaryExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		val nextKind = cursor.nextOrNull?.kind ?: return false
		if (nextKind != DOUBLE_PLUS && nextKind != DOUBLE_MINUS) return false
		return cursor.current.isIdentifier()
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'${cursor.current.value}' 前不允许有表达式", cursor.current)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): SuffixUnaryExpression {
		return parseSuffixUnaryExpression()
	}
}