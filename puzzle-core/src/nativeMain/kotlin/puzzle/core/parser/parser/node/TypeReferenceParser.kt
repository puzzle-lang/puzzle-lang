package puzzle.core.parser.parser.node

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.node.LambdaType
import puzzle.core.parser.ast.node.NamedType
import puzzle.core.parser.ast.node.TypeReference
import puzzle.core.parser.parser.binding.parseLambdaParameters

class TypeReferenceParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<TypeReferenceParser>(::TypeReferenceParser)
	
	context(_: PzlContext)
	fun parse(
		isSupportedLambdaType: Boolean = false,
		isSupportedNullable: Boolean = true
	): TypeReference {
		return if (cursor.match(PzlTokenType.LPAREN)) {
			parseLambdaType(isSupportedLambdaType, isSupportedNullable)
		} else {
			parseNamedType(isSupportedNullable)
		}
	}
	
	context(_: PzlContext)
	private fun parseLambdaType(
		isSupportedLambdaType: Boolean,
		isSupportedNullable: Boolean
	): TypeReference {
		val parameters = parseLambdaParameters(cursor)
		if (cursor.match(PzlTokenType.ARROW)) {
			if (!isSupportedLambdaType) {
				syntaxError("不支持 Lambda 类型", cursor.previous)
			}
			val returnTypes = mutableListOf<TypeReference>()
			if (cursor.match(PzlTokenType.LBRACKET)) {
				while (!cursor.match(PzlTokenType.RBRACKET)) {
					returnTypes += this.parse(isSupportedLambdaType = true)
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
				returnTypes += this.parse(isSupportedLambdaType = true)
			}
			val isNullable = cursor.match(PzlTokenType.QUESTION)
			if (isNullable) {
				if (!isSupportedNullable) {
					syntaxError("不支持可空类型", cursor.previous)
				}
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
				isNullable = isNullable
			)
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
			val isNullable = cursor.match(PzlTokenType.QUESTION)
			if (isNullable && !isSupportedNullable) {
				syntaxError("不支持可空类型", cursor.previous)
			}
			val type = parameter.typeReference.type
			return TypeReference(
				type = type,
				isNullable = isNullable
			)
		}
	}
	
	context(_: PzlContext)
	private fun parseNamedType(isSupportedNullable: Boolean): TypeReference {
		val type = mutableListOf<String>()
		do {
			cursor.expect(PzlTokenType.IDENTIFIER, "无法识别标识符")
			type += cursor.previous.value
		} while (cursor.match(PzlTokenType.DOT))
		val isNullable = cursor.match(PzlTokenType.QUESTION)
		if (isNullable && !isSupportedNullable) {
			syntaxError("不支持可空类型", cursor.previous)
		}
		return TypeReference(
			type = NamedType(type),
			isNullable = isNullable,
		)
	}
}