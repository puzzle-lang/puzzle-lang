package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.LambdaExpression
import puzzle.core.parser.parser.parameter.parameter.parseLambdaParameterReferences
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.SymbolKind.HASH

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseLambdaExpression(): LambdaExpression {
	val containLabel = cursor.offset(-2).kind == HASH
	val start = if (containLabel) cursor.offset(-3).location else cursor.previous.location
	val label = if (containLabel) cursor.offset(-3).toIdentifier() else null
	val references = parseLambdaParameterReferences()
	val body = parseStatements()
	val end = cursor.previous.location
	return LambdaExpression(label, references, body, start span end)
}