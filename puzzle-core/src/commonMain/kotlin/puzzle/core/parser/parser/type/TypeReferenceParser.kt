package puzzle.core.parser.parser.type

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.type.LambdaType
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.parser.parameter.parameter.parseLambdaParameters
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.End.RPAREN
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
	return if (cursor.check(LPAREN)) {
		parseLambdaTypeReference(allowLambdaType, allowNullable)
	} else {
		parseNamedTypeReference(allowNullable)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseLambdaTypeReference(
	allowLambdaType: Boolean,
	allowNullable: Boolean,
): TypeReference {
	val start = cursor.current.location
	val parameters = parseLambdaParameters()
	if (cursor.match(ARROW)) {
		if (!allowLambdaType) {
			syntaxError("不支持 lambda 类型", cursor.previous)
		}
		val returnTypes = mutableListOf<TypeReference>()
		if (cursor.match(LBRACKET)) {
			while (!cursor.match(RBRACKET)) {
				returnTypes += parseTypeReference(allowLambdaType = true)
				if (!cursor.check(RBRACKET)) {
					cursor.expect(COMMA, "lambda 缺少 ','")
				}
			}
			if (returnTypes.isEmpty()) {
				syntaxError("lambda 多返回值类型语法错误", cursor.previous)
			}
			if (returnTypes.size == 1) {
				syntaxError("lambda 多返回值类型至少需要2个", cursor.previous)
			}
		} else {
			returnTypes += parseTypeReference(allowLambdaType = true)
		}
		val end = cursor.previous.location
		val type = LambdaType(parameters, returnTypes, start span end)
		var isNullable = false
		while (cursor.match(QUESTION)) {
			isNullable = true
		}
		if (isNullable) {
			if (!allowNullable) {
				syntaxError("不支持可空类型", cursor.previous)
			}
			val token = cursor.offset(offset = -2)
			if (token.kind != RPAREN) {
				syntaxError("lambda 表示可空前必须加 ')'", token)
			}
		}
		val location = start span cursor.previous.location
		return TypeReference(type, location, isNullable)
	} else {
		if (parameters.size != 1) {
			if (allowLambdaType) {
				syntaxError("lambda 缺少 '->'", cursor.current)
			} else {
				syntaxError("不支持 lambda 类型", cursor.offset(offset = -2))
			}
		}
		val parameter = parameters.single()
		if (parameter.name != null) {
			syntaxError("语法错误", cursor.offset(offset = -4))
		}
		var isNullable = false
		while (cursor.match(QUESTION)) {
			isNullable = true
		}
		if (isNullable && !allowNullable) {
			syntaxError("不支持可空类型", cursor.previous)
		}
		val end = cursor.previous.location
		return TypeReference(
			type = parameter.type.type,
			location = start span end,
			isNullable = isNullable
		)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseNamedTypeReference(allowNullable: Boolean): TypeReference {
	val type = parseNamedType()
	var isNullable = false
	while (cursor.match(QUESTION)) {
		isNullable = true
	}
	if (isNullable && !allowNullable) {
		syntaxError("不支持可空类型", cursor.previous)
	}
	val end = cursor.previous.location
	return TypeReference(type, type.location span end, isNullable)
}