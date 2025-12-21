package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.InvokeExpression
import puzzle.core.parser.parser.expression.parseInvokeExpression
import puzzle.core.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.token.kinds.BracketKind.Start.LPAREN

object InvokeExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<InvokeExpression> {
	
	private val kinds = arrayOf(LPAREN, LBRACKET)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		if (left == null) return false
		return kinds.any { cursor.match(it) }
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("${cursor.previous.value} 前未解析到表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): InvokeExpression {
		return parseInvokeExpression(left)
	}
}