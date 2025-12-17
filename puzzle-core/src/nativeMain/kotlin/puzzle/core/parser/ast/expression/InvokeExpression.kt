package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode
import puzzle.core.token.SourceLocation

sealed interface InvokeExpression : Expression {
	
	val callee: Expression
	
	val arguments: List<Argument>
}

@Serializable
class CallExpression(
	override val callee: Expression,
	override val location: SourceLocation,
	override val arguments: List<Argument> = emptyList(),
) : InvokeExpression

@Serializable
class IndexAccessExpression(
	override val callee: Expression,
	override val location: SourceLocation,
	override val arguments: List<Argument> = emptyList(),
) : InvokeExpression

@Serializable
class Argument(
	val name: IdentifierExpression?,
	val expression: Expression,
	override val location: SourceLocation,
) : AstNode