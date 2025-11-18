package puzzle.core.parser

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import puzzle.core.lexer.PzlTokenType

@Serializable
enum class Operator(
	val priority: Int,
	val associativity: Associativity
) {
	@SerialName("++")
	DOUBLE_PLUS(
		priority = 12,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("--")
	DOUBLE_MINUS(
		priority = 12,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("!")
	NOT(
		priority = 11,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("~")
	BIT_NOT(
		priority = 11,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("**")
	DOUBLE_STAR(
		priority = 10,
		associativity = Associativity.RIGHT,
	),
	
	@SerialName("*")
	STAR(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("/")
	SLASH(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("%")
	PERCENT(
		priority = 9,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("+")
	PLUS(
		priority = 8,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("-")
	MINUS(
		priority = 8,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("<<")
	SHL(
		priority = 7,
		associativity = Associativity.LEFT,
	),
	
	@SerialName(">>")
	SHR(
		priority = 7,
		associativity = Associativity.LEFT
	),
	
	@SerialName(">>>")
	USHR(
		priority = 7,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("==")
	EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("!=")
	NOT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("===")
	TRIPLE_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("!==")
	TRIPLE_NOT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName(">")
	GT(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName(">=")
	GT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("<")
	LT(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("<=")
	LT_EQUALS(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("~>")
	IN(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("!>")
	NOT_IN(
		priority = 6,
		associativity = Associativity.NONE,
	),
	
	@SerialName("&")
	BIT_AND(
		priority = 5,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("^")
	BIT_XOR(
		priority = 4,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("|")
	BIT_OR(
		priority = 3,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("&&")
	AND(
		priority = 2,
		associativity = Associativity.LEFT,
	),
	
	@SerialName("||")
	OR(
		priority = 1,
		associativity = Associativity.LEFT,
	)
}

enum class Associativity {
	LEFT,
	NONE,
	RIGHT
}

fun PzlTokenType.toOperator(): Operator = when (this) {
	PzlTokenType.DOUBLE_PLUS -> Operator.DOUBLE_PLUS
	PzlTokenType.DOUBLE_MINUS -> Operator.DOUBLE_MINUS
	PzlTokenType.BANG -> Operator.NOT
	PzlTokenType.BIT_NOT -> Operator.BIT_NOT
	PzlTokenType.DOUBLE_STAR -> Operator.DOUBLE_STAR
	PzlTokenType.STAR -> Operator.STAR
	PzlTokenType.SLASH -> Operator.SLASH
	PzlTokenType.PERCENT -> Operator.PERCENT
	PzlTokenType.PLUS -> Operator.PLUS
	PzlTokenType.MINUS -> Operator.MINUS
	PzlTokenType.SHL -> Operator.SHL
	PzlTokenType.SHR -> Operator.SHR
	PzlTokenType.USHR -> Operator.USHR
	PzlTokenType.EQUALS -> Operator.EQUALS
	PzlTokenType.NOT_EQUALS -> Operator.NOT_EQUALS
	PzlTokenType.TRIPLE_EQUALS -> Operator.TRIPLE_EQUALS
	PzlTokenType.TRIPLE_NOT_EQUALS -> Operator.TRIPLE_NOT_EQUALS
	PzlTokenType.GT -> Operator.GT
	PzlTokenType.GT_EQUALS -> Operator.GT_EQUALS
	PzlTokenType.LT -> Operator.LT
	PzlTokenType.LT_EQUALS -> Operator.LT_EQUALS
	PzlTokenType.IN -> Operator.IN
	PzlTokenType.NOT_IN -> Operator.NOT_IN
	PzlTokenType.BIT_AND -> Operator.BIT_AND
	PzlTokenType.BIT_XOR -> Operator.BIT_XOR
	PzlTokenType.BIT_OR -> Operator.BIT_OR
	PzlTokenType.AND -> Operator.AND
	PzlTokenType.OR -> Operator.OR
	else -> error("不支持的运算符")
}