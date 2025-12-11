package puzzle.core.token

import kotlinx.serialization.Serializable

@Serializable
sealed interface WhiteSpaceKind : PzlTokenKind {
	
	@Serializable
	object NEWLINE : WhiteSpaceKind {
		override val value = "NEWLINE"
	}
	
	@Serializable
	object WHITESPACE : WhiteSpaceKind {
		override val value = "WHITESPACE"
	}
	
	@Serializable
	object TAB : WhiteSpaceKind {
		override val value = "TAB"
	}
}