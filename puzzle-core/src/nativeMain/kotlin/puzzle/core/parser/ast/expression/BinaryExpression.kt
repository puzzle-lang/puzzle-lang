package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.OperatorKind

@Serializable
class BinaryExpression(
	val left: Expression,
	val operator: OperatorKind,
	val right: Expression,
) : Expression