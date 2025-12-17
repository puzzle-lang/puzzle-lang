package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.SourceLocation
import puzzle.core.token.kinds.AccessKind
import puzzle.core.token.span
import puzzle.core.util.AccessKindSerializer

@Serializable
class PropertyAccessExpression(
	val receiver: Expression,
	@Serializable(with = AccessKindSerializer::class)
	val access: AccessKind,
	val expression: Expression,
	override val location: SourceLocation = receiver.location span expression.location,
) : Expression