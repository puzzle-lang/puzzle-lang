package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.Operator
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression,
	override val location: SourceLocation = operator.location span expression.location,
) : Expression