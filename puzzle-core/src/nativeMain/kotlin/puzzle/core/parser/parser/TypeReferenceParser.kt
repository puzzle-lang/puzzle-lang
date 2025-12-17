package puzzle.core.parser.parser

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.LambdaType
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.parser.argument.parseTypeArguments
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierString
import puzzle.core.parser.parser.parameter.parameter.parseLambdaParameters
import puzzle.core.token.kinds.AccessKind
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.SeparatorKind
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.model.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeReference(
	isSupportedLambdaType: Boolean = false,
	isSupportedNullable: Boolean = true,
): TypeReference {
	return if (cursor.match(BracketKind.Start.LPAREN)) {
		parseLambdaType(isSupportedLambdaType, isSupportedNullable)
	} else {
		parseNamedType(isSupportedNullable)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseLambdaType(
	isSupportedLambdaType: Boolean,
	isSupportedNullable: Boolean,
): TypeReference {
	val start = cursor.current.location
	val parameters = parseLambdaParameters()
	if (cursor.match(SymbolKind.ARROW)) {
		if (!isSupportedLambdaType) {
			syntaxError("不支持 Lambda 类型", cursor.previous)
		}
		val returnTypes = mutableListOf<TypeReference>()
		if (cursor.match(BracketKind.Start.LBRACKET)) {
			while (!cursor.match(BracketKind.End.RBRACKET)) {
				returnTypes += parseTypeReference(isSupportedLambdaType = true)
				if (!cursor.check(BracketKind.End.RBRACKET)) {
					cursor.expect(SeparatorKind.COMMA, "Lambda 缺少 ','")
				}
			}
			if (returnTypes.isEmpty()) {
				syntaxError("Lambda 多返回值类型语法错误", cursor.previous)
			}
			if (returnTypes.size == 1) {
				syntaxError("Lambda 多返回值类型至少需要2个", cursor.previous)
			}
		} else {
			returnTypes += parseTypeReference(isSupportedLambdaType = true)
		}
		val end = cursor.previous.location
		val type = LambdaType(parameters, returnTypes, start span end)
		var isNullable = false
		while (cursor.match(SymbolKind.QUESTION)) {
			isNullable = true
		}
		if (isNullable) {
			if (!isSupportedNullable) {
				syntaxError("不支持可空类型", cursor.previous)
			}
			val token = cursor.offset(offset = -2)
			if (token.kind != BracketKind.End.RPAREN) {
				syntaxError("Lambda 表示可空前必须加 ')'", token)
			}
		}
		val location = start span cursor.previous.location
		return TypeReference(type, location, isNullable)
	} else {
		if (parameters.size != 1) {
			if (isSupportedLambdaType) {
				syntaxError("Lambda 缺少 '->'", cursor.current)
			} else {
				syntaxError("不支持 Lambda 类型", cursor.offset(offset = -2))
			}
		}
		val parameter = parameters.single()
		if (parameter.name != null) {
			syntaxError("语法错误", cursor.offset(offset = -4))
		}
		var isNullable = false
		while (cursor.match(SymbolKind.QUESTION)) {
			isNullable = true
		}
		if (isNullable && !isSupportedNullable) {
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
private fun parseNamedType(isSupportedNullable: Boolean): TypeReference {
	val start = cursor.current.location
	val segments = mutableListOf<String>()
	do {
		segments += parseIdentifierString(IdentifierTarget.TYPE_REFERENCE)
	} while (cursor.match(AccessKind.DOT))
	val typeArguments = parseTypeArguments()
	val location = start span cursor.previous.location
	val type = NamedType(segments, location, typeArguments)
	var isNullable = false
	while (cursor.match(SymbolKind.QUESTION)) {
		isNullable = true
	}
	if (isNullable && !isSupportedNullable) {
		syntaxError("不支持可空类型", cursor.previous)
	}
	val end = cursor.previous.location
	return TypeReference(type, start span end, isNullable)
}