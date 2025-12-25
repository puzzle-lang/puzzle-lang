package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.model.SourceLocation

@Serializable
class IfExpression(
	val condition: Expression,
	val thenStatements: List<Statement>,
	val elseStatements: List<Statement>,
	override val location: SourceLocation
) : Expression