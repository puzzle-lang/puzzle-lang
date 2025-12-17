package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.SourceLocation
import puzzle.core.token.span

@Serializable
class ElvisExpression(
	val left: Expression,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression