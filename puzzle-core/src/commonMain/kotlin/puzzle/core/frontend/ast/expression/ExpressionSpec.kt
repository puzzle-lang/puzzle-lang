package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.AstNode

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