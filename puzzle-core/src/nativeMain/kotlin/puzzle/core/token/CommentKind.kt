package puzzle.core.token

import kotlinx.serialization.Serializable

@Serializable
sealed interface CommentKind : PzlTokenKind {
	
	@Serializable
	class SingleLine(override val value: String) : CommentKind
	
	@Serializable
	class MultiLine(override val value: String) : CommentKind
}