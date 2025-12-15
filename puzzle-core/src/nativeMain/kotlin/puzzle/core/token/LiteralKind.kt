package puzzle.core.token

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
	
	sealed class BooleanKind(
		override val value: KString
	) : LiteralKind, KeywordKind {
		
		companion object {
			
			val kinds = fastSetOf<BooleanKind>(TRUE, FALSE)
		}
		
		object TRUE : BooleanKind("true")
		
		object FALSE : BooleanKind("false")
	}
	
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
		override val value: KString,
		val system: NumberSystem,
		val type: NumberLiteralType
	) : LiteralKind
}

enum class NumberSystem {
	BINARY,
	DECIMAL,
	HEX
}

enum class NumberLiteralType(
	val isDecimal: Boolean,
	val isUnsigned: Boolean,
	val byteSize: Int
) {
	INT(isDecimal = false, isUnsigned = false, byteSize = 4),
	LONG(isDecimal = false, isUnsigned = false, byteSize = 8),
	UINT(isDecimal = false, isUnsigned = true, byteSize = 4),
	ULONG(isDecimal = false, isUnsigned = true, byteSize = 8),
	FLOAT(isDecimal = true, isUnsigned = false, byteSize = 4),
	DOUBLE(isDecimal = true, isUnsigned = false, byteSize = 8);
	
	companion object {
		
		fun get(
			isDecimal: Boolean,
			isUnsigned: Boolean,
			byteSize: Int
		): NumberLiteralType {
			return NumberLiteralType.entries.first {
				it.isDecimal == isDecimal && it.isUnsigned == isUnsigned && it.byteSize == byteSize
			}
		}
	}
}