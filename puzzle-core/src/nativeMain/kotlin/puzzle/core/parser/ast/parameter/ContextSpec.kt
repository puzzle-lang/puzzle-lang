package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.model.SourceLocation
import puzzle.core.model.span

@Serializable
class ContextSpec(
	val receivers: List<ContextReceiver>,
	val isInherited: Boolean,
	override val location: SourceLocation,
) : AstNode

@Serializable
class ContextReceiver(
	val name: IdentifierExpression,
	val type: TypeReference,
	override val location: SourceLocation = name.location span type.location,
) : AstNode