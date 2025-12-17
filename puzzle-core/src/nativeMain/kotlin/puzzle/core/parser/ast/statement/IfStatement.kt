package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.token.SourceLocation

@Serializable
class IfStatement(
	val condition: Expression,
	val thenStatements: List<Statement>,
	override val location: SourceLocation,
) : Statement