package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable

@Serializable
class NumberLiteral(val value: String) : Expression

@Serializable
class StringLiteral(val value: String) : Expression

@Serializable
class BooleanLiteral private constructor(
	val value: Boolean
) : Expression {
	
	companion object {
		
		val True = BooleanLiteral(true)
		
		val False = BooleanLiteral(false)
	}
}

@Serializable
class CharLiteral(val value: String) : Expression

@Serializable
data object ThisLiteral : Expression

@Serializable
data object SuperLiteral : Expression

@Serializable
data object NullLiteral : Expression

@Serializable
class IdentifierExpression(val name: String) : Expression