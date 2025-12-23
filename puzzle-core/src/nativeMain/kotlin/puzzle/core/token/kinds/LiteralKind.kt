package puzzle.core.token.kinds

import puzzle.core.collections.fastSetOf
import puzzle.core.collections.mergeFastSets
import puzzle.core.model.SourceLocation
import puzzle.core.token.PzlToken

sealed interface LiteralKind : PzlTokenKind {
	
	companion object {
		
		val keywordKinds = mergeFastSets<KeywordKind>(
			BooleanKind.kinds,
			fastSetOf(NULL)
		)
	}
	
	object NULL : LiteralKind, KeywordKind {
		
		override val value = "null"
	}
}

sealed class BooleanKind(
	override val value: String,
) : LiteralKind, KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<BooleanKind>(TRUE, FALSE)
	}
	
	object TRUE : BooleanKind("true")
	
	object FALSE : BooleanKind("false")
}

class CharKind(
	override val value: String,
) : LiteralKind

class NumberKind(
	override val value: String,
	val system: NumberSystem,
	val type: NumberLiteralType,
) : LiteralKind

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
		): NumberLiteralType = entries.first {
			it.isDecimal == isDecimal && it.isUnsigned == isUnsigned && it.is8Byte == is8Byte
		}
	}
}

sealed interface StringKind : LiteralKind {
	
	class Text(
		override val value: String,
	) : StringKind
	
	class Template(
		val parts: List<Part>,
	) : StringKind {
		
		override val value = "[STRING TEMPLATE]"
		
		sealed interface Part {
			
			val location: SourceLocation
			
			class Text(
				val value: String,
				override val location: SourceLocation,
			) : Part
			
			class Expression(
				val tokens: List<PzlToken>,
				override val location: SourceLocation,
			) : Part
		}
	}
}