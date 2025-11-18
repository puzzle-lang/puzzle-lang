package puzzle.core.parser.node

import kotlinx.serialization.Serializable
import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.parameter.parser.parseLambdaParameters

@Serializable
data class TypeReference(
	val type: Type,
	val isNullable: Boolean = false,
	val isArray: Boolean = false
)

@Serializable
sealed interface Type

@Serializable
data class NamedType(
	val value: String
) : Type

@Serializable
data class LambdaType(
	val parameters: List<Parameter>,
	val returnTypes: List<TypeReference>
) : Type

class TypeReferenceParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(isSupportedLambdaType: Boolean = true): TypeReference {
		return if (ctx.match(PzlTokenType.LPAREN)) {
			parseLambdaType(isSupportedLambdaType)
		} else {
			parseNamedType()
		}
	}
	
	context(_: PzlContext)
	private fun parseLambdaType(
		isSupportedLambdaType: Boolean
	): TypeReference {
		val parameters = parseLambdaParameters(ctx)
		if (ctx.match(PzlTokenType.ARROW)) {
			if (!isSupportedLambdaType) {
				syntaxError("不支持 Lambda 类型", ctx.previous)
			}
			val returnTypes = mutableListOf<TypeReference>()
			if (ctx.match(PzlTokenType.LBRACKET)) {
				while (!ctx.match(PzlTokenType.RBRACKET)) {
					returnTypes += this.parse()
					if (!ctx.check(PzlTokenType.RBRACKET)) {
						ctx.expect(PzlTokenType.COMMA, "Lambda 缺少 ','")
					}
				}
				if (returnTypes.isEmpty()) {
					syntaxError("Lambda 多返回值类型语法错误", ctx.previous)
				}
				if (returnTypes.size == 1) {
					syntaxError("Lambda 多返回值类型至少需要2个", ctx.previous)
				}
			} else {
				returnTypes += this.parse()
			}
			val isArray = ctx.match(PzlTokenType.LBRACKET)
			if (isArray) {
				val type = ctx.peek(offset = -2)?.type
				if (type != PzlTokenType.IDENTIFIER && type != PzlTokenType.RPAREN) {
					syntaxError("语法错误", ctx.previous)
				}
				ctx.expect(PzlTokenType.RBRACKET, "数组类型缺少 ']'")
			}
			val isNullable = ctx.match(PzlTokenType.QUESTION)
			if (isNullable) {
				val type = ctx.peek(offset = -2)?.type
				if (
					type != PzlTokenType.RPAREN &&
					type != PzlTokenType.IDENTIFIER &&
					(type != PzlTokenType.RBRACKET || ctx.peek(offset = -3)?.type != PzlTokenType.LBRACKET)
				) {
					syntaxError("语法错误", ctx.previous)
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
				syntaxError("Lambda 缺少 '->'", ctx.current)
			}
			val parameter = parameters.single()
			if (parameter.name != null) {
				syntaxError("语法错误", ctx.peek(offset = -4)!!)
			}
			val isArray = ctx.match(PzlTokenType.LBRACKET)
			if (isArray) {
				ctx.expect(PzlTokenType.RBRACKET, "数组类型缺少 ']'")
			}
			val isNullable = ctx.match(PzlTokenType.QUESTION)
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
			ctx.expect(PzlTokenType.IDENTIFIER, "无法识别标识符")
			type += ctx.previous.value
		} while (ctx.match(PzlTokenType.DOT))
		val isArray = ctx.match(PzlTokenType.LBRACKET)
		if (isArray) {
			ctx.expect(PzlTokenType.RBRACKET, "数组类型缺少 ']'")
		}
		val isNullable = ctx.match(PzlTokenType.QUESTION)
		return TypeReference(
			type = NamedType(type.joinToString(".")),
			isNullable = isNullable,
			isArray = isArray
		)
	}
}