package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.LambdaParameterReference
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.tryParseIdentifier
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.ARROW
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseLambdaParameterReferences(): List<LambdaParameterReference> {
	return buildList {
		while (!cursor.match(ARROW)) {
			val name = tryParseIdentifier(IdentifierTarget.LAMBDA_IDENTIFIER) ?: break
			when {
				cursor.match(COLON) -> {
					val type = parseTypeReference(allowLambdaType = true)
					this += LambdaParameterReference(name, type)
					if (!cursor.check(ARROW)) {
						cursor.expect(COMMA, "lambda 参数缺少 ','")
					}
				}
				
				cursor.match(ARROW) -> {
					this += LambdaParameterReference(name)
					break
				}
				
				cursor.match(COMMA) -> {
					this += LambdaParameterReference(name)
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