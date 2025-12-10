package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Expression

@Serializable
class ExpressionStatement(
	val expression: Expression,
) : Statement