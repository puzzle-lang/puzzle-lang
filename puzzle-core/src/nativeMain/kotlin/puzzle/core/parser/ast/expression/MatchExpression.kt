package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.token.SourceLocation

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
	val elseStatements: List<Statement>?,
	override val location: SourceLocation,
) : MatchExpression

@Serializable
class MatchArm(
	val patterns: List<Expression>,
	val guard: Expression?,
	val statements: List<Statement>,
	override val location: SourceLocation,
) : AstNode