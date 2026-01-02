package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.statement.Statement

@Serializable
class IfExpression(
	val condition: Expression,
	val thenStatements: List<Statement>,
	val elseStatements: List<Statement>,
	override val location: SourceLocation,
) : Expression, CompoundAssignable