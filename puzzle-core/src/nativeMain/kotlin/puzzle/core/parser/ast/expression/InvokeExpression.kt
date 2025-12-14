package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.BracketKind

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
	val startTokenKind: BracketKind.Start,
	val endTokenKind: BracketKind.End
) {
	CALL(BracketKind.Start.LPAREN, BracketKind.End.RPAREN),
	INDEX_ACCESS(BracketKind.Start.LBRACKET, BracketKind.End.RBRACKET),
}