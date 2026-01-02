package puzzle.core.frontend.parser.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.type.TypeReference
import puzzle.core.frontend.parser.ast.expression.Identifier

@Serializable
class LambdaParameter(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation,
) : AstNode