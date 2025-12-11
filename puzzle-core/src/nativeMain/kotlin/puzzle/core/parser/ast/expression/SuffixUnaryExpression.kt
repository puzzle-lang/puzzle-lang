package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.OperatorKind

@Serializable
class SuffixUnaryExpression(
	val expression: Expression,
	val operator: OperatorKind
) : Expression