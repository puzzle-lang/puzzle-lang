package puzzle.core.token

import kotlinx.serialization.Serializable
import puzzle.core.collections.fastSetOf
import puzzle.core.collections.mergeFastSets
import kotlin.String as KString

sealed interface LiteralKind : PzlTokenKind {
	
	companion object {
		
		val keywordKinds = mergeFastSets<KeywordKind>(
			BooleanKind.kinds,
			fastSetOf<KeywordKind>(NULL)
		)
	}
	
	@Serializable
	sealed class BooleanKind(
		override val value: KString
	) : LiteralKind, KeywordKind {
		
		companion object {
			
			val kinds = fastSetOf<BooleanKind>(TRUE, FALSE)
		}
		
		@Serializable
		object TRUE : BooleanKind("true")
		
		@Serializable
		object FALSE : BooleanKind("false")
	}
	
	@Serializable
	object NULL : LiteralKind, KeywordKind {
		override val value = "null"
	}
	
	class String(
		override val value: KString
	) : LiteralKind
	
	class Char(
		override val value: KString
	) : LiteralKind
	
	class Number(
		override val value: KString
	) : LiteralKind
}