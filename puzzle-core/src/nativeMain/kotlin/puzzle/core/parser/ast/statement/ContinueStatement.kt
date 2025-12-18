package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.expression.IdentifierExpression

@Serializable
class ContinueStatement(
	val label: IdentifierExpression?,
	override val location: SourceLocation,
) : Statement