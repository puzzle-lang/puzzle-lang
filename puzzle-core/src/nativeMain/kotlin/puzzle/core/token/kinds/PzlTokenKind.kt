package puzzle.core.token.kinds

sealed interface PzlTokenKind {
	
	val value: String
}

class IdentifierKind(
	override val value: String
) : PzlTokenKind