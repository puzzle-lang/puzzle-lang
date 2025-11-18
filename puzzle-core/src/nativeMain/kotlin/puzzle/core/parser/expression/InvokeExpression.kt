package puzzle.core.parser.expression

import kotlinx.serialization.Serializable
import puzzle.core.lexer.PzlTokenType

sealed interface InvokeExpression : Expression {
	
	val callee: Expression
	
	val arguments: List<Argument>
}

@Serializable
data class CallExpression(
	override val callee: Expression,
	override val arguments: List<Argument> = emptyList(),
) : InvokeExpression

@Serializable
data class IndexAccessExpression(
	override val callee: Expression,
	override val arguments: List<Argument> = emptyList(),
) : InvokeExpression

@Serializable
data class Argument(
	val name: String?,
	val expression: Expression,
)

enum class InvokeType(
	val startTokenType: PzlTokenType,
	val endTokenType: PzlTokenType,
) {
	CALL(PzlTokenType.LPAREN, PzlTokenType.RPAREN),
	INDEX_ACCESS(PzlTokenType.LBRACKET, PzlTokenType.RBRACKET),
}