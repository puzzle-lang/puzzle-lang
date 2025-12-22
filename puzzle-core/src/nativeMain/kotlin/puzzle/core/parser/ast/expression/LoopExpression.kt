package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.statement.Statement

@Serializable
class LoopExpression(
	val label: Identifier?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Expression