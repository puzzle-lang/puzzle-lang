package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.token.SourceLocation

@Serializable
class VariableDeclarationStatement(
	val name: IdentifierExpression,
	val type: TypeReference?,
	val isMutable: Boolean,
	val initializer: Expression?,
	override val location: SourceLocation,
) : Statement