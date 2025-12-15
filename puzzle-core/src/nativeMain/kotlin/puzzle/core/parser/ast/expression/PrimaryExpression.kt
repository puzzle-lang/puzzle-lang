package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.NumberLiteralType
import puzzle.core.token.NumberSystem

@Serializable
class NumberLiteral(
	val value: String,
	val system: NumberSystem,
	val type: NumberLiteralType
) : Expression

@Serializable
class StringLiteral(val value: String) : Expression

@Serializable
class BooleanLiteral private constructor(
	val value: Boolean
) : Expression {
	
	companion object {
		
		val TRUE = BooleanLiteral(true)
		
		val FALSE = BooleanLiteral(false)
	}
}

@Serializable
class CharLiteral(val value: String) : Expression

@Serializable
object ThisLiteral : Expression

@Serializable
object SuperLiteral : Expression

@Serializable
object NullLiteral : Expression

@Serializable
class IdentifierExpression(val name: String) : Expression