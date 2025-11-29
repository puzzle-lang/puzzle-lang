package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

/**
 * 括号表达式
 */
@Serializable
class GroupingExpression(
	val expression: Expression
) : Expression