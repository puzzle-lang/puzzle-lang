package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Argument

@Serializable
class InitStatement(
	val type: TypeReference,
	val arguments: List<Argument>,
	val isSafe: Boolean,
	override val location: SourceLocation,
) : Statement