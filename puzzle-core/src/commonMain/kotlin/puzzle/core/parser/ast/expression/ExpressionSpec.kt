package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AstNode

@Serializable
sealed interface ExpressionSpec : AstNode

@Serializable
class SingleExpressionSpec(
	val expression: Expression,
	override val location: SourceLocation = expression.location,
) : ExpressionSpec

@Serializable
class MultiExpressionSpec(
	val expressions: List<Expression>,
	override val location: SourceLocation,
) : ExpressionSpec