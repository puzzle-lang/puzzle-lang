package puzzle.core.frontend.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.expression.ExpressionSpec
import puzzle.core.frontend.parser.ast.expression.Identifier

@Serializable
class BreakStatement(
	val label: Identifier?,
	val expression: ExpressionSpec?,
	override val location: SourceLocation,
) : Statement