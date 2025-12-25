package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.model.SourceLocation

@Serializable
class VariableDeclarationStatement(
	val name: Identifier,
	val type: TypeReference?,
	val isMutable: Boolean,
	val initializer: Expression?,
	override val location: SourceLocation,
) : Statement