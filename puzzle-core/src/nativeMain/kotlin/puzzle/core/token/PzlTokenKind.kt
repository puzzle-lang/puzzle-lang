package puzzle.core.token

import kotlinx.serialization.Serializable

sealed interface PzlTokenKind {
	
	val value: String
}

class IdentifierKind(
	override val value: String
) : PzlTokenKind