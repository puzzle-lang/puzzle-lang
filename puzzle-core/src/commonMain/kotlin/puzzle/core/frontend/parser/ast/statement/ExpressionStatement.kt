package puzzle.core.frontend.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.parser.ast.expression.Expression
import puzzle.core.frontend.model.SourceLocation

@Serializable
class ExpressionStatement(
	val expression: Expression,
	override val location: SourceLocation = expression.location,
) : Statement