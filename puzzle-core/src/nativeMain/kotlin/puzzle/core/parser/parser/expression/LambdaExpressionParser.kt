package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.LambdaExpression
import puzzle.core.parser.parser.parameter.parameter.parseLambdaParameterReferences
import puzzle.core.parser.parser.statement.parseStatements

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseLambdaExpression(): LambdaExpression {
	val start = cursor.previous.location
	val references = parseLambdaParameterReferences()
	val body = parseStatements()
	val end = cursor.previous.location
	return LambdaExpression(references, body, start span end)
}