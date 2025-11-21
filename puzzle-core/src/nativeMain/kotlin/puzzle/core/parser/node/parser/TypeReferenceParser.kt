package puzzle.core.parser.node.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.node.LambdaType
import puzzle.core.parser.node.NamedType
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.parameter.parser.parseLambdaParameters

class TypeReferenceParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(isSupportedLambdaType: Boolean = true): TypeReference {
		return if (cursor.match(PzlTokenType.LPAREN)) {
			parseLambdaType(isSupportedLambdaType)
		} else {
			parseNamedType()
		}
	}
	
	context(_: PzlContext)
	private fun parseLambdaType(
		isSupportedLambdaType: Boolean
	): TypeReference {
		val parameters = parseLambdaParameters(cursor)
		if (cursor.match(PzlTokenType.ARROW)) {
			if (!isSupportedLambdaType) {
				syntaxError("不支持 Lambda 类型", cursor.previous)
			}
			val returnTypes = mutableListOf<TypeReference>()
			if (cursor.match(PzlTokenType.LBRACKET)) {
				while (!cursor.match(PzlTokenType.RBRACKET)) {
					returnTypes += this.parse()
					if (!cursor.check(PzlTokenType.RBRACKET)) {
						cursor.expect(PzlTokenType.COMMA, "Lambda 缺少 ','")
					}
				}
				if (returnTypes.isEmpty()) {
					syntaxError("Lambda 多返回值类型语法错误", cursor.previous)
				}
				if (returnTypes.size == 1) {
					syntaxError("Lambda 多返回值类型至少需要2个", cursor.previous)
				}
			} else {
				returnTypes += this.parse()
			}
			val isArray = cursor.match(PzlTokenType.LBRACKET)
			if (isArray) {
				val type = cursor.offsetOrNull(offset = -2)?.type
				if (type != PzlTokenType.IDENTIFIER && type != PzlTokenType.RPAREN) {
					syntaxError("语法错误", cursor.previous)
				}
				cursor.expect(PzlTokenType.RBRACKET, "数组类型缺少 ']'")
			}
			val isNullable = cursor.match(PzlTokenType.QUESTION)
			if (isNullable) {
				val type = cursor.offsetOrNull(offset = -2)?.type
				if (
					type != PzlTokenType.RPAREN &&
					type != PzlTokenType.IDENTIFIER &&
					(type != PzlTokenType.RBRACKET || cursor.offsetOrNull(offset = -3)?.type != PzlTokenType.LBRACKET)
				) {
					syntaxError("语法错误", cursor.previous)
				}
			}
			return TypeReference(
				type = LambdaType(
					parameters = parameters,
					returnTypes = returnTypes
				),
				isNullable = isNullable,
				isArray = isArray
			)
		} else {
			if (parameters.size != 1) {
				syntaxError("Lambda 缺少 '->'", cursor.current)
			}
			val parameter = parameters.single()
			if (parameter.name != null) {
				syntaxError("语法错误", cursor.offset(offset = -4))
			}
			val isArray = cursor.match(PzlTokenType.LBRACKET)
			if (isArray) {
				cursor.expect(PzlTokenType.RBRACKET, "数组类型缺少 ']'")
			}
			val isNullable = cursor.match(PzlTokenType.QUESTION)
			val type = parameter.typeReference.type
			return TypeReference(
				type = type,
				isNullable = isNullable,
				isArray = isArray
			)
		}
	}
	
	context(_: PzlContext)
	private fun parseNamedType(): TypeReference {
		val type = mutableListOf<String>()
		do {
			cursor.expect(PzlTokenType.IDENTIFIER, "无法识别标识符")
			type += cursor.previous.value
		} while (cursor.match(PzlTokenType.DOT))
		val isArray = cursor.match(PzlTokenType.LBRACKET)
		if (isArray) {
			cursor.expect(PzlTokenType.RBRACKET, "数组类型缺少 ']'")
		}
		val isNullable = cursor.match(PzlTokenType.QUESTION)
		return TypeReference(
			type = NamedType(type),
			isNullable = isNullable,
			isArray = isArray
		)
	}
}