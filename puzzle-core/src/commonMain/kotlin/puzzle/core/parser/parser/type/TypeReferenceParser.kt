package puzzle.core.parser.parser.type

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.type.LambdaType
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.parser.parameter.parameter.parseLambdaParameters
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.ARROW
import puzzle.core.token.kinds.SymbolKind.QUESTION

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeReference(
	allowLambdaType: Boolean = false,
	allowNullable: Boolean = true,
): TypeReference {
	val start = cursor.current.location
	if (!cursor.check(LPAREN)) {
		val type = parseNamedType()
		val isNullable = isNullable(allowNullable)
		val end = cursor.previous.location
		return TypeReference(type, start span end, isNullable)
	}
	val parameters = parseLambdaParameters()
	if (!cursor.match(ARROW)) {
		if (parameters.size != 1 || parameters.first().name != null) {
			syntaxError("lambda 表达式缺少 '->'", cursor.current)
		}
		val parameter = parameters.single()
		val isNullable = isNullable(allowNullable) || parameter.type.isNullable
		val end = cursor.previous.location
		return TypeReference(parameter.type.type, start span end, isNullable)
	}
	if (!allowLambdaType) {
		syntaxError("不支持 lambda 表达式", cursor.previous)
	}
	val returnTypes = buildList {
		if (cursor.match(LBRACKET)) {
			while (!cursor.match(RBRACKET)) {
				this += parseTypeReference(allowLambdaType, allowNullable)
				if (!cursor.check(RBRACKET)) {
					cursor.expect(COMMA, "lambda 表达式缺少 ','")
				}
			}
			if (this.isEmpty()) {
				syntaxError("lambda 表达式的多返回值类型缺失", cursor.previous)
			}
			if (this.size == 1) {
				syntaxError("lambda 表达式的多返回值类型至少需要2个", cursor.previous)
			}
		} else {
			this += parseTypeReference(allowLambdaType, allowNullable)
		}
	}
	val end = cursor.previous.location
	val type = LambdaType(parameters, returnTypes, start span end)
	val isNullable = isNullable(allowNullable)
	val location = start span cursor.previous.location
	return TypeReference(type, location, isNullable)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun isNullable(allowNullable: Boolean): Boolean {
	var isNullable = false
	while (cursor.match(QUESTION)) {
		isNullable = true
		if (!allowNullable) {
			syntaxError("不支持可空类型", cursor.previous)
		}
	}
	return isNullable
}