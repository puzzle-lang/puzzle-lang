package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.ast.Operator

@Serializable
class SuffixUnaryExpression(
	val expression: Expression,
	val operator: Operator,
	override val location: SourceLocation = expression.location span operator.location,
) : Expression