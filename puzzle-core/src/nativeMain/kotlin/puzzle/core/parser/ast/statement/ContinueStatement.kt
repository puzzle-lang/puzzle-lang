package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class ContinueStatement(
	val label: Identifier?,
	override val location: SourceLocation,
) : Statement