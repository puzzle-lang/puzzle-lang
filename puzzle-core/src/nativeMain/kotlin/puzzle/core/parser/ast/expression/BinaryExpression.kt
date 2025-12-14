package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.OperatorKind
import puzzle.core.util.OperatorKindSerializer

@Serializable
class BinaryExpression(
	val left: Expression,
	@Serializable(with = OperatorKindSerializer::class)
	val operator: OperatorKind,
	val right: Expression,
) : Expression