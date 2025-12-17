package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.statement.Statement

@Serializable
sealed interface MatchExpression : Expression

@Serializable
class MatchConditionExpression(
	val cases: List<MatchCase>,
	override val location: SourceLocation,
	val elseStatements: List<Statement>? = null,
) : MatchExpression

@Serializable
class MatchCase(
	val condition: Expression,
	val statements: List<Statement>,
	override val location: SourceLocation,
) : AstNode

@Serializable
class MatchPatternExpression(
	val subject: Expression,
	val arms: List<MatchArm>,
	override val location: SourceLocation,
	val elseStatements: List<Statement>? = null,
) : MatchExpression

@Serializable
class MatchArm(
	val patterns: List<MatchPattern>,
	val guard: Expression?,
	val statements: List<Statement>,
	override val location: SourceLocation,
) : AstNode

@Serializable
sealed interface MatchPattern

@Serializable
class ExpressionMatchPattern(
	val expression: Expression,
) : MatchPattern

@Serializable
class IsTypeMatchPattern(
	val type: TypeReference,
) : MatchPattern