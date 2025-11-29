package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.lexer.PzlTokenType

sealed interface InvokeExpression : Expression {
	
	val callee: Expression
	
	val arguments: List<Argument>
}

@Serializable
class CallExpression(
	override val callee: Expression,
	override val arguments: List<Argument> = emptyList(),
) : InvokeExpression

@Serializable
class IndexAccessExpression(
	override val callee: Expression,
	override val arguments: List<Argument> = emptyList(),
) : InvokeExpression

@Serializable
class Argument(
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