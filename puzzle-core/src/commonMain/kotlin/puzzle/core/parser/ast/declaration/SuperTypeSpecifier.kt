package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.expression.Argument
import puzzle.core.parser.ast.type.NamedType

@Serializable
sealed interface SuperTypeSpecifier : AstNode {
	val type: NamedType
}

@Serializable
class SuperConstructorCall(
	override val type: NamedType,
	val arguments: List<Argument>,
	override val location: SourceLocation,
) : SuperTypeSpecifier

@Serializable
class SuperTypeReference(
	override val type: NamedType,
	override val location: SourceLocation,
) : SuperTypeSpecifier