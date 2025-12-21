package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.AsExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.expression.parseAsExpression
import puzzle.core.token.kinds.SymbolKind.QUESTION
import puzzle.core.token.kinds.TypeOperatorKind.AS

object AsExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<AsExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(AS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		val isSafe = cursor.check(QUESTION)
		syntaxError(
			"${if (isSafe) "as?" else "as"} 前未解析到表达式",
			cursor.offset(if (isSafe) -2 else -1)
		)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): AsExpression {
		return parseAsExpression(left)
	}
}