package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Expression

@Serializable
class IfStatement(
	val condition: Expression,
	val thenStatements: List<Statement>
) : Statement