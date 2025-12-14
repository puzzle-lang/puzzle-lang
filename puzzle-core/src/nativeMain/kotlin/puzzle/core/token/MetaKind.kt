package puzzle.core.token

sealed interface MetaKind : PzlTokenKind {
	
	object EOF : MetaKind {
		override val value = "EOF"
	}
}