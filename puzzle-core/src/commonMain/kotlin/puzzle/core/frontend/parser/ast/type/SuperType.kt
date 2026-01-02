package puzzle.core.frontend.parser.ast.type

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.expression.Argument

@Serializable
sealed interface SuperType : AstNode {
	val type: NamedType
}

@Serializable
class SuperConstructorCall(
	override val type: NamedType,
	val arguments: List<Argument>,
	override val location: SourceLocation,
) : SuperType

@Serializable
class SuperTypeReference(
	override val type: NamedType,
	override val location: SourceLocation,
) : SuperType