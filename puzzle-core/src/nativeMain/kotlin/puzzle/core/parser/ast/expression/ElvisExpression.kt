package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class ElvisExpression(
	val left: Expression,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression