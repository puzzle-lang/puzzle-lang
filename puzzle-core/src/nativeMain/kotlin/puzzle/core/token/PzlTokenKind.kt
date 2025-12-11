package puzzle.core.token

import kotlinx.serialization.Serializable

@Serializable
sealed interface PzlTokenKind {
	
	val value: String
}

@Serializable
class IdentifierKind(
	override val value: String
) : PzlTokenKind