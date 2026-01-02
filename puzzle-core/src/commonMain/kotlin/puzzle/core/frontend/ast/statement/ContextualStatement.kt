package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.expression.Argument

@Serializable
sealed interface ContextualStatement : Statement {
	
	val arguments: List<Argument>
}

@Serializable
class ThisStatement(
	override val arguments: List<Argument>,
	override val location: SourceLocation,
) : ContextualStatement

@Serializable
class SuperStatement(
	override val arguments: List<Argument>,
	override val location: SourceLocation,
) : ContextualStatement