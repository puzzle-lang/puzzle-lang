package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.Assignment
import puzzle.core.frontend.ast.expression.Expression

@Serializable
sealed interface AssignmentStatement : Statement {
	
	val target: Expression
	
	val assignment: Assignment
	
	val value: Expression
}

@Serializable
class DirectAssignmentStatement(
	override val target: Expression,
	override val assignment: Assignment,
	override val value: Expression,
	override val location: SourceLocation,
) : AssignmentStatement

@Serializable
class CompoundAssignmentStatement(
	override val target: Expression,
	override val assignment: Assignment,
	override val value: Expression,
	override val location: SourceLocation,
) : AssignmentStatement