package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.expression.ExpressionSpec
import puzzle.core.frontend.ast.expression.Identifier

@Serializable
class ReturnStatement(
	val label: Identifier?,
	val expression: ExpressionSpec?,
	override val location: SourceLocation,
) : Statement