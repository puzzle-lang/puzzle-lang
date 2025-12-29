package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.type.TypeReference

@Serializable
class VariableDeclarationStatement(
	val variableSpec: VariableSpec,
	val initializer: Expression?,
	override val location: SourceLocation,
) : Statement

@Serializable
sealed interface VariableSpec : AstNode

@Serializable
class SingleVariableSpec(
	val isMutable: Boolean,
	val name: Identifier,
	val type: TypeReference?,
	override val location: SourceLocation,
) : VariableSpec

@Serializable
class MultiVariableSpec(
	val patterns: List<VariablePattern>,
	override val location: SourceLocation,
) : VariableSpec

@Serializable
class VariablePattern(
	val isMutable: Boolean,
	val name: Identifier,
	val type: TypeReference?,
	override val location: SourceLocation,
) : AstNode