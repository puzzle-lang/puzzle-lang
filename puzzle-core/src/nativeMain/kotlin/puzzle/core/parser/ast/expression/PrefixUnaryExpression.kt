package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.OperatorKind

@Serializable
class PrefixUnaryExpression(
	val operator: OperatorKind,
	val expression: Expression
) : Expression