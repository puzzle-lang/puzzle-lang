package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class BreakStatement(
	val label: Identifier?,
	val expression: Expression?,
	override val location: SourceLocation,
) : Statement