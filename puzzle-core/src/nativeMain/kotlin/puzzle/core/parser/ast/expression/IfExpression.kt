package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.statement.Statement

@Serializable
class IfExpression(
	val condition: Expression,
	val thenStatements: List<Statement>,
	val elseStatements: List<Statement>
) : Expression