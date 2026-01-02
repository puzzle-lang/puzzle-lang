package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.expression.Argument
import puzzle.core.frontend.ast.type.NamedType

@Serializable
class InitStatement(
	val type: NamedType,
	val arguments: List<Argument>,
	val isSafe: Boolean,
	override val location: SourceLocation,
) : Statement