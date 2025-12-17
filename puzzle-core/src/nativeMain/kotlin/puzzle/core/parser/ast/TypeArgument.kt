package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.token.SourceLocation
import puzzle.core.token.span

@Serializable
class TypeArgument(
	val name: IdentifierExpression?,
	val type: TypeReference,
	override val location: SourceLocation =
		if (name != null) name.location span type.location else type.location,
) : AstNode