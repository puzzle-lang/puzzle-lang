package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.ast.Operator

@Serializable
class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression, CompoundAssignable