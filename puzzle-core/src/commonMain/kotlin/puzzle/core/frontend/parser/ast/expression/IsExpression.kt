package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.ast.type.TypeReference

@Serializable
class IsExpression(
	val expression: Expression,
	val type: TypeReference,
	val negated: Boolean,
	override val location: SourceLocation = expression.location span type.location,
) : Expression, CompoundAssignable