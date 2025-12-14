package puzzle.core.token

import kotlinx.serialization.Serializable

sealed interface WhiteSpaceKind : PzlTokenKind {
	
	object NEWLINE : WhiteSpaceKind {
		override val value = "NEWLINE"
	}
	
	object WHITESPACE : WhiteSpaceKind {
		override val value = "WHITESPACE"
	}
	
	object TAB : WhiteSpaceKind {
		override val value = "TAB"
	}
}