package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation

@Serializable
class IdentifierExpression(
	val name: String,
	override val location: SourceLocation,
) : Expression