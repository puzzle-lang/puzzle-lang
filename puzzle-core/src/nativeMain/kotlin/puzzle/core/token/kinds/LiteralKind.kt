package puzzle.core.token.kinds

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
		override val value: KString,
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
	
	class StringKind(
		override val value: KString,
	) : LiteralKind
	
	class CharKind(
		override val value: KString,
	) : LiteralKind
	
	class NumberKind(
		override val value: KString,
		val system: NumberSystem,
		val type: NumberLiteralType,
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
	val is8Byte: Boolean,
) {
	INT(isDecimal = false, isUnsigned = false, is8Byte = false),
	LONG(isDecimal = false, isUnsigned = false, is8Byte = true),
	UINT(isDecimal = false, isUnsigned = true, is8Byte = false),
	ULONG(isDecimal = false, isUnsigned = true, is8Byte = true),
	FLOAT(isDecimal = true, isUnsigned = false, is8Byte = false),
	DOUBLE(isDecimal = true, isUnsigned = false, is8Byte = true);
	
	companion object {
		
		fun get(
			isDecimal: Boolean,
			isUnsigned: Boolean,
			is8Byte: Boolean,
		): NumberLiteralType {
			return entries.first {
				it.isDecimal == isDecimal && it.isUnsigned == isUnsigned && it.is8Byte == is8Byte
			}
		}
	}
}