package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.LiteralExpression
import puzzle.core.parser.parser.expression.parseLiteralExpression
import puzzle.core.token.kinds.LiteralKind

object LiteralExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<LiteralExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		val kind = cursor.current.kind
		return if (kind is LiteralKind) {
			cursor.advance()
			true
		} else false
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("字面量前不允许有表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(): LiteralExpression {
		return parseLiteralExpression()
	}
}