package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IdentifierExpression

@Serializable
class BreakStatement(
	val label: IdentifierExpression?,
	val expression: Expression?,
	override val location: SourceLocation,
) : Statement