package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class IsExpression(
	val expression: Expression,
	val type: TypeReference,
	val negated: Boolean,
	override val location: SourceLocation = expression.location span type.location,
) : Expression