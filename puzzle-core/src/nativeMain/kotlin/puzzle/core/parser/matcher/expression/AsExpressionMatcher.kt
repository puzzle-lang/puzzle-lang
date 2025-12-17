package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.AsExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parseAsExpression
import puzzle.core.token.kinds.SymbolKind.QUESTION
import puzzle.core.token.kinds.TypeOperatorKind.AS

object AsExpressionMatcher : ExpressionMatcher<AsExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(AS, QUESTION) || cursor.match(AS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): AsExpression {
		val isSafe = cursor.previous == QUESTION
		if (left == null) {
			syntaxError("${if (isSafe) "as?" else "as"} 操作符左侧未解析到表达式", cursor.current)
		}
		return parseAsExpression(left, isSafe)
	}
}