package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.type.TypeReference

@Serializable
sealed interface ContextSpec<out R : ContextReceiver> : AstNode {
	
	val receivers: List<R>
	
	val isPropagate: Boolean
}

@Serializable
sealed interface ContextReceiver : AstNode {
	
	val type: TypeReference
}

@Serializable
class DeclarationContextSpec(
	override val receivers: List<DeclarationContextReceiver>,
	override val isPropagate: Boolean,
	override val location: SourceLocation,
) : ContextSpec<DeclarationContextReceiver>

@Serializable
class DeclarationContextReceiver(
	val name: Identifier,
	override val type: TypeReference,
	override val location: SourceLocation = name.location span type.location,
) : ContextReceiver

@Serializable
class LambdaContextSpec(
	override val receivers: List<LambdaContextReceiver>,
	override val isPropagate: Boolean,
	override val location: SourceLocation,
) : ContextSpec<LambdaContextReceiver>

@Serializable
class LambdaContextReceiver(
	override val type: TypeReference,
	val typeExpansion: TypeExpansion?,
	override val location: SourceLocation,
) : ContextReceiver