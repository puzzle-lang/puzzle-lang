package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.TypeReference

@Serializable
class VariableDeclarationStatement(
	val name: String,
	val initializer: Expression?,
	val isVariable: Boolean,
	val type: TypeReference?
) : Statement