package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ExpressionStatement
import puzzle.core.parser.parser.expression.parseExpressionChain

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExpressionStatement(): ExpressionStatement {
	val expression = parseExpressionChain()
	return ExpressionStatement(expression)
}