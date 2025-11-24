package puzzle.core.parser.parser.binding.generic

enum class GenericTarget(
	val displayName: String,
	val isSupportedGeneric: Boolean,
	val isSupportedVariance: Boolean
) {
	FUN(
		displayName = "函数",
		isSupportedGeneric = true,
		isSupportedVariance = false
	),
	CLASS(
		displayName = "类",
		isSupportedGeneric = true,
		isSupportedVariance = true
	),
	UNIQUE(
		displayName = "单例类",
		isSupportedGeneric = false,
		isSupportedVariance = false
	),
	TRAIT(
		displayName = "特征",
		isSupportedGeneric = true,
		isSupportedVariance = true
	),
	STRUCT(
		displayName = "结构体",
		isSupportedGeneric = true,
		isSupportedVariance = true
	),
	ENUM(
		displayName = "枚举",
		isSupportedGeneric = true,
		isSupportedVariance = true
	),
	ANNOTATION(
		displayName = "注解",
		isSupportedGeneric = false,
		isSupportedVariance = false
	),
	EXTENSION(
		displayName = "扩展",
		isSupportedGeneric = true,
		isSupportedVariance = true
	)
}