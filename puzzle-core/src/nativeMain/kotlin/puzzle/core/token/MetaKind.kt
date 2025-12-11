package puzzle.core.token

import kotlinx.serialization.Serializable

@Serializable
sealed interface MetaKind : PzlTokenKind {
	
	@Serializable
	object EOF : MetaKind {
		override val value = "EOF"
	}
}