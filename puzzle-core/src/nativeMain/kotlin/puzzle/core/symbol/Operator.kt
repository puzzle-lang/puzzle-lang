package puzzle.core.symbol

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import puzzle.core.lexer.PzlTokenType

@Serializable
enum class Operator(
	val priority: Int,
	val associativity: Associativity,
	val tokenType: PzlTokenType
) {
	@SerialName("++")
	DOUBLE_PLUS(
		priority = 12,
		associativity = Associativity.RIGHT,
		tokenType = PzlTokenType.DOUBLE_PLUS
	),
	
	@SerialName("--")
	DOUBLE_MINUS(
		priority = 12,
		associativity = Associativity.RIGHT,
		tokenType = PzlTokenType.DOUBLE_MINUS
	),
	
	@SerialName("!")
	NOT(
		priority = 11,
		associativity = Associativity.RIGHT,
		tokenType = PzlTokenType.NOT
	),
	
	@SerialName("~")
	BIT_NOT(
		priority = 11,
		associativity = Associativity.RIGHT,
		tokenType = PzlTokenType.BIT_NOT
	),
	
	@SerialName("**")
	DOUBLE_STAR(
		priority = 10,
		associativity = Associativity.RIGHT,
		tokenType = PzlTokenType.DOUBLE_STAR
	),
	
	@SerialName("*")
	STAR(
		priority = 9,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.STAR
	),
	
	@SerialName("/")
	SLASH(
		priority = 9,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.SLASH,
	),
	
	@SerialName("%")
	PERCENT(
		priority = 9,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.PERCENT
	),
	
	@SerialName("+")
	PLUS(
		priority = 8,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.PLUS
	),
	
	@SerialName("-")
	MINUS(
		priority = 8,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.MINUS
	),
	
	@SerialName("<<")
	SHL(
		priority = 7,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.SHL
	),
	
	@SerialName(">>")
	SHR(
		priority = 7,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.SHR
	),
	
	@SerialName(">>>")
	USHR(
		priority = 7,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.USHR
	),
	
	@SerialName("==")
	EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.EQUALS
	),
	
	@SerialName("!=")
	NOT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.NOT_EQUALS
	),
	
	@SerialName("===")
	TRIPLE_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.TRIPLE_EQUALS
	),
	
	@SerialName("!==")
	TRIPLE_NOT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.TRIPLE_NOT_EQUALS
	),
	
	@SerialName(">")
	GT(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.GT
	),
	
	@SerialName(">=")
	GT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.GT_EQUALS
	),
	
	@SerialName("<")
	LT(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.LT
	),
	
	@SerialName("<=")
	LT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.LT_EQUALS
	),
	
	@SerialName("~>")
	CONTAINS(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.CONTAINS
	),
	
	@SerialName("!~>")
	NOT_CONTAINS(
		priority = 6,
		associativity = Associativity.NONE,
		tokenType = PzlTokenType.NOT_CONTAINS
	),
	
	@SerialName("&")
	BIT_AND(
		priority = 5,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.BIT_AND
	),
	
	@SerialName("^")
	BIT_XOR(
		priority = 4,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.BIT_XOR
	),
	
	@SerialName("|")
	BIT_OR(
		priority = 3,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.BIT_OR
	),
	
	@SerialName("&&")
	AND(
		priority = 2,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.AND
	),
	
	@SerialName("||")
	OR(
		priority = 1,
		associativity = Associativity.LEFT,
		tokenType = PzlTokenType.OR
	)
}

enum class Associativity {
	LEFT,
	NONE,
	RIGHT
}

fun PzlTokenType.toOperator(): Operator {
	return Operator.entries.find { this == it.tokenType }
		?: error("不支持的运算符")
}