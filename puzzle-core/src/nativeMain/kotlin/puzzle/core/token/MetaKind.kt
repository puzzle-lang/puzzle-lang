package puzzle.core.token

sealed class MetaKind(
	override val value: String,
) : PzlTokenKind {
	
	object EOF : MetaKind("EOF")
}