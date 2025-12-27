package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.parser.ast.type.NamedType

@Serializable
class InitStatement(
	val type: NamedType,
	val arguments: List<Argument>,
	val isSafe: Boolean,
	override val location: SourceLocation,
) : Statement