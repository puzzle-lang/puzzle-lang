package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.statement.Statement

@Serializable
sealed interface MatchExpression : Expression

@Serializable
class MatchConditionExpression(
	val cases: List<MatchCase>,
	val elseStatements: List<Statement>?
) : MatchExpression

@Serializable
class MatchCase(
	val condition: Expression,
	val statements: List<Statement>
)

@Serializable
class MatchPatternExpression(
	val subject: Expression,
	val arms: List<MatchArm>,
	val elseStatements: List<Statement>?
) : MatchExpression

@Serializable
class MatchArm(
	val patterns: List<Expression>,
	val guard: Expression?,
	val statements: List<Statement>
)