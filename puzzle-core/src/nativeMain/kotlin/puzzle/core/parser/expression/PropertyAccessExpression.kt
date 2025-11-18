package puzzle.core.parser.expression

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import puzzle.core.lexer.PzlTokenType

@Serializable
data class PropertyAccessExpression(
	val receiver: Expression,
	val operator: AccessOperator,
	val expression: Expression
) : Expression

@Serializable
enum class AccessOperator {
	
	@SerialName(".")
	DOT,
	
	@SerialName("?.")
	QUESTION_DOT,
	
	@SerialName("::")
	DOUBLE_COLON
}

fun PzlTokenType.toAccessOperator(): AccessOperator = when (this) {
	PzlTokenType.DOT -> AccessOperator.DOT
	PzlTokenType.QUESTION_DOT -> AccessOperator.QUESTION_DOT
	PzlTokenType.DOUBLE_COLON -> AccessOperator.DOUBLE_COLON
	else -> error("不支持的操作符")
}