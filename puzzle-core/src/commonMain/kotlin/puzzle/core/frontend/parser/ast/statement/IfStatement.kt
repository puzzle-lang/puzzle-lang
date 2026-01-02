package puzzle.core.frontend.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.parser.ast.expression.Expression
import puzzle.core.frontend.model.SourceLocation

@Serializable
class IfStatement(
	val condition: Expression,
	val thenStatements: List<Statement>,
	override val location: SourceLocation,
) : Statement