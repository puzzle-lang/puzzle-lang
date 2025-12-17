package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.model.SourceLocation

@Serializable
class ExpressionStatement(
	val expression: Expression,
	override val location: SourceLocation = expression.location,
) : Statement