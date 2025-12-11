@file:Suppress("ClassName")

package puzzle.core.token

import kotlinx.serialization.Serializable
import puzzle.core.collections.fastSetOf
import puzzle.core.collections.mergeFastSets

@Serializable
sealed interface SymbolKind : PzlTokenKind {
	
	companion object {
		
		val kinds = mergeFastSets(
			OperatorKind.kinds,
			AssignmentKind.kinds,
			AccessKind.kinds,
			BracketKind.kinds,
			SeparatorKind.kinds,
			fastSetOf<SymbolKind>(
				COLON, QUESTION, ELVIS,
				AT,
				ARROW,
				INDEX_GET, INDEX_SET,
				RANGE_TO, RANGE_UNTIL
			)
		)
	}
	
	@Serializable
	object COLON : SymbolKind {
		override val value = ":"
	}
	
	@Serializable
	object QUESTION : SymbolKind {
		override val value = "?"
	}
	
	@Serializable
	object ELVIS : SymbolKind {
		override val value = "?:"
	}
	
	@Serializable
	object AT : SymbolKind {
		override val value = "@"
	}
	
	@Serializable
	object ARROW : SymbolKind {
		override val value = "->"
	}
	
	@Serializable
	object INDEX_GET : SymbolKind {
		override val value = "[]"
	}
	
	@Serializable
	object INDEX_SET : SymbolKind {
		override val value = "[]="
	}
	
	@Serializable
	object RANGE_TO : SymbolKind {
		override val value = ".."
	}
	
	@Serializable
	object RANGE_UNTIL : SymbolKind {
		override val value = "..<"
	}
}

@Serializable
sealed class OperatorKind(
	override val value: String,
	val priority: Int,
	val assoc: Assoc,
) : SymbolKind {
	
	companion object {
		
		val kinds = fastSetOf<OperatorKind>(
			DOUBLE_PLUS, DOUBLE_MINUS,
			NOT, BIT_NOT,
			DOUBLE_STAR,
			STAR, SLASH, PERCENT,
			PLUS, MINUS,
			SHL, SHR, USHR,
			EQUALS, NOT_EQUALS, TRIPLE_EQUALS, TRIPLE_NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, CONTAINS, NOT_CONTAINS,
			BIT_AND,
			BIT_XOR,
			BIT_OR,
			AND,
			OR
		)
	}
	
	@Serializable
	object DOUBLE_PLUS : OperatorKind("++", 12, Assoc.RIGHT)
	
	@Serializable
	object DOUBLE_MINUS : OperatorKind("--", 12, Assoc.RIGHT)
	
	@Serializable
	object NOT : OperatorKind("!", 11, Assoc.RIGHT)
	
	@Serializable
	object BIT_NOT : OperatorKind("~", 11, Assoc.RIGHT)
	
	@Serializable
	object DOUBLE_STAR : OperatorKind("**", 10, Assoc.RIGHT)
	
	@Serializable
	object STAR : OperatorKind("*", 9, Assoc.LEFT)
	
	@Serializable
	object SLASH : OperatorKind("/", 9, Assoc.LEFT)
	
	@Serializable
	object PERCENT : OperatorKind("%", 9, Assoc.LEFT)
	
	@Serializable
	object PLUS : OperatorKind("+", 8, Assoc.LEFT)
	
	@Serializable
	object MINUS : OperatorKind("-", 8, Assoc.LEFT)
	
	@Serializable
	object SHL : OperatorKind("<<", 7, Assoc.LEFT)
	
	@Serializable
	object SHR : OperatorKind(">>", 7, Assoc.LEFT)
	
	@Serializable
	object USHR : OperatorKind(">>>", 7, Assoc.LEFT)
	
	@Serializable
	object EQUALS : OperatorKind("==", 6, Assoc.NONE)
	
	@Serializable
	object NOT_EQUALS : OperatorKind("!=", 6, Assoc.NONE)
	
	@Serializable
	object TRIPLE_EQUALS : OperatorKind("===", 6, Assoc.NONE)
	
	@Serializable
	object TRIPLE_NOT_EQUALS : OperatorKind("!==", 6, Assoc.NONE)
	
	@Serializable
	object GT : OperatorKind(">", 6, Assoc.NONE)
	
	@Serializable
	object GT_EQUALS : OperatorKind(">=", 6, Assoc.NONE)
	
	@Serializable
	object LT : OperatorKind("<", 6, Assoc.NONE)
	
	@Serializable
	object LT_EQUALS : OperatorKind("<=", 6, Assoc.NONE)
	
	@Serializable
	object CONTAINS : OperatorKind("~>", 6, Assoc.NONE)
	
	@Serializable
	object NOT_CONTAINS : OperatorKind("!~>", 6, Assoc.NONE)
	
	@Serializable
	object BIT_AND : OperatorKind("&", 5, Assoc.LEFT)
	
	@Serializable
	object BIT_XOR : OperatorKind("^", 4, Assoc.LEFT)
	
	@Serializable
	object BIT_OR : OperatorKind("|", 3, Assoc.LEFT)
	
	@Serializable
	object AND : OperatorKind("&&", 2, Assoc.LEFT)
	
	@Serializable
	object OR : OperatorKind("||", 1, Assoc.LEFT)
}

@Serializable
enum class Assoc {
	LEFT,
	NONE,
	RIGHT
}

@Serializable
sealed class AssignmentKind(
	override val value: String
) : SymbolKind {
	
	companion object {
		
		val kinds = fastSetOf<AssignmentKind>(
			ASSIGN, QUESTION_ASSIGN,
			PLUS_ASSIGN, MINUS_ASSIGN, STAR_ASSIGN, SLASH_ASSIGN, PERCENT_ASSIGN,
		)
	}
	
	@Serializable
	object ASSIGN : AssignmentKind("=")
	
	@Serializable
	object QUESTION_ASSIGN : AssignmentKind("?=")
	
	@Serializable
	object PLUS_ASSIGN : AssignmentKind("+=")
	
	@Serializable
	object MINUS_ASSIGN : AssignmentKind("-=")
	
	@Serializable
	object STAR_ASSIGN : AssignmentKind("*=")
	
	@Serializable
	object SLASH_ASSIGN : AssignmentKind("/=")
	
	@Serializable
	object PERCENT_ASSIGN : AssignmentKind("%=")
}

@Serializable
sealed class AccessKind(
	override val value: String,
) : SymbolKind {
	
	companion object {
		
		val kinds = fastSetOf<AccessKind>(DOT, QUESTION_DOT, DOUBLE_COLON)
	}
	
	@Serializable
	object DOT : AccessKind(".")
	
	@Serializable
	object QUESTION_DOT : AccessKind("?.")
	
	@Serializable
	object DOUBLE_COLON : AccessKind("::")
}

@Serializable
sealed class BracketKind(
	override val value: String
) : SymbolKind {
	
	companion object {
		
		val kinds = fastSetOf<BracketKind>(LPAREN, RPAREN, LBRACKET, RBRACKET, LBRACE, RBRACE)
	}
	
	@Serializable
	object LPAREN : BracketKind("(")
	
	@Serializable
	object RPAREN : BracketKind(")")
	
	@Serializable
	object LBRACKET : BracketKind("[")
	
	@Serializable
	object RBRACKET : BracketKind("]")
	
	@Serializable
	object LBRACE : BracketKind("{")
	
	@Serializable
	object RBRACE : BracketKind("}")
}

@Serializable
sealed class SeparatorKind(
	override val value: String,
) : SymbolKind {
	
	companion object {
		
		val kinds = fastSetOf<SeparatorKind>(COMMA, SEMICOLON)
	}
	
	@Serializable
	object COMMA : SeparatorKind(",")
	
	@Serializable
	object SEMICOLON : SeparatorKind(";")
}