package puzzle.core.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class LambdaParameter(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation,
) : AstNode