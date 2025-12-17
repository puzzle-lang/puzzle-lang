package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.token.SourceLocation

@Serializable
sealed interface SuperType : AstNode {
	val type: TypeReference
}

@Serializable
class SuperClass(
	override val type: TypeReference,
	override val location: SourceLocation,
	val arguments: List<Argument> = emptyList(),
) : SuperType

@Serializable
class SuperTrait(
	override val type: TypeReference,
	override val location: SourceLocation,
) : SuperType