package puzzle.core.token

sealed interface PzlTokenKind {
	
	val value: String
}

class IdentifierKind(
	override val value: String
) : PzlTokenKind