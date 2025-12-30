package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.LambdaExpression
import puzzle.core.parser.ast.parameter.ParameterReference
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.*

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

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseLambdaParameterReferences(): List<ParameterReference> {
	return buildList {
		while (!cursor.match(ARROW)) {
			val name = tryParseIdentifier(IdentifierTarget.LAMBDA_PARAMETER_REFERENCE) ?: break
			when {
				cursor.match(COLON) -> {
					val type = parseTypeReference(allowLambda = true)
					this += ParameterReference(name, type)
					if (!cursor.check(ARROW)) {
						cursor.expect(COMMA, "lambda 参数引用列表缺少 ','")
					}
				}
				
				cursor.match(ARROW) -> {
					this += ParameterReference(name)
					break
				}
				
				cursor.match(COMMA) -> {
					this += ParameterReference(name)
					continue
				}
				
				else -> {
					cursor.retreat()
					break
				}
			}
		}
	}
}