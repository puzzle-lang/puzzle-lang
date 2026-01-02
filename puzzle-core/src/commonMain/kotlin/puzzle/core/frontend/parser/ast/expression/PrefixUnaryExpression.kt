package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.ast.Operator

@Serializable
class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression,
	override val location: SourceLocation = operator.location span expression.location,
) : Expression