package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.OperatorKind
import puzzle.core.util.OperatorKindSerializer

@Serializable
class PrefixUnaryExpression(
	@Serializable(with = OperatorKindSerializer::class)
	val operator: OperatorKind,
	val expression: Expression
) : Expression