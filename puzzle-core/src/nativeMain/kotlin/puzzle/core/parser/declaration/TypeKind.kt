package puzzle.core.parser.declaration

import puzzle.core.parser.declaration.TypeKind.*


enum class TypeKind {
	CLASS,
	SINGLE,
	TRAIT,
	STRUCT,
	ENUM,
	ENUM_ENTRY,
	ANNOTATION,
	EXTENSION
}

fun TypeKind.getDisplayName(): String {
	return when (this) {
		CLASS -> "类"
		SINGLE -> "单例类"
		TRAIT -> "特征"
		STRUCT -> "结构体"
		ENUM -> "枚举"
		ENUM_ENTRY -> "枚举实例"
		ANNOTATION -> "注解"
		EXTENSION -> "扩展"
	}
}