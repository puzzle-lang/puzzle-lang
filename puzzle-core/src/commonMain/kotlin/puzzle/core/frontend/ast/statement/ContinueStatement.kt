package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.expression.Identifier

@Serializable
class ContinueStatement(
	val label: Identifier?,
	override val location: SourceLocation,
) : Statement