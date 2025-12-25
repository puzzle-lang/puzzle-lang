package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.TernaryExpression
import puzzle.core.parser.parser.expression.parseTernaryExpression
import puzzle.core.token.kinds.SymbolKind.QUESTION

object TernaryExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<TernaryExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(QUESTION)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'?' 前未解析到表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): TernaryExpression {
		return parseTernaryExpression(left)
	}
}