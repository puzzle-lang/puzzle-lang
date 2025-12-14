package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.AccessKind
import puzzle.core.util.AccessKindSerializer

@Serializable
class PropertyAccessExpression(
	val receiver: Expression,
	@Serializable(with = AccessKindSerializer::class)
	val access: AccessKind,
	val expression: Expression
) : Expression