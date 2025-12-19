package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class ForStatement(
	val pattern: ForPattern,
	val iterable: Expression,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Statement

@Serializable
sealed interface ForPattern

@Serializable
class ValuePattern(
	val value: Identifier,
) : ForPattern

@Serializable
class IndexValuePattern(
	val index: Identifier,
	val value: Identifier,
) : ForPattern