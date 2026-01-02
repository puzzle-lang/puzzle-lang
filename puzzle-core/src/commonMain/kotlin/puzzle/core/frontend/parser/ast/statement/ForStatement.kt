package puzzle.core.frontend.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.expression.Expression
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.parameter.ParameterReference

@Serializable
class ForStatement(
	val label: Identifier?,
	val pattern: ForPattern,
	val iterable: Expression,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Statement

@Serializable
sealed interface ForPattern : AstNode

@Serializable
class ForValuePattern(
	val reference: ParameterReference,
	override val location: SourceLocation = reference.location,
) : ForPattern

@Serializable
class ForDestructurePattern(
	val references: List<ParameterReference>,
	override val location: SourceLocation,
) : ForPattern