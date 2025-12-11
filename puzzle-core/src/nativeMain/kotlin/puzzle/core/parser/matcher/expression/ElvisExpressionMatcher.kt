package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.ElvisExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parseElvisExpression
import puzzle.core.token.SymbolKind

object ElvisExpressionMatcher : ExpressionMatcher<ElvisExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(SymbolKind.ELVIS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): ElvisExpression {
		if (left == null) {
			syntaxError("'?:' 操作符前必须跟表达式", cursor.previous)
		}
		return parseElvisExpression(left)
	}
}