package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.AccessKind

@Serializable
class PropertyAccessExpression(
	val receiver: Expression,
	val operator: AccessKind,
	val expression: Expression
) : Expression