package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.Operator
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression