package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.Operator
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class SuffixUnaryExpression(
	val expression: Expression,
	val operator: Operator,
	override val location: SourceLocation = expression.location span operator.location,
) : Expression