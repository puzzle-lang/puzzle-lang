package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.Assignment
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class AssignmentStatement(
	val target: Identifier,
	val assignment: Assignment,
	val value: Expression,
	override val location: SourceLocation,
) : Statement