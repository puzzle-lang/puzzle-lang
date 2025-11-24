package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

/**
 * 括号表达式
 */
@Serializable
data class GroupingExpression(
	val expression: Expression
) : Expression