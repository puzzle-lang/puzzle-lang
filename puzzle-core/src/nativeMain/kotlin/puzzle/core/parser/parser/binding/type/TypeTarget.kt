package puzzle.core.parser.parser.binding.type

enum class TypeTarget(
    val displayName: String,
    val isSupportedType: Boolean,
    val isSupportedVariance: Boolean
) {
    FUN(
        displayName = "函数",
        isSupportedType = true,
        isSupportedVariance = false
    ),
    PROPERTY(
        displayName = "属性",
        isSupportedType = true,
        isSupportedVariance = false
    ),
    CLASS(
        displayName = "类",
        isSupportedType = true,
        isSupportedVariance = true
    ),
    UNIQUE(
        displayName = "单例类",
        isSupportedType = false,
        isSupportedVariance = false
    ),
    TRAIT(
        displayName = "特征",
        isSupportedType = true,
        isSupportedVariance = true
    ),
    STRUCT(
        displayName = "结构体",
        isSupportedType = true,
        isSupportedVariance = true
    ),
    ENUM(
        displayName = "枚举",
        isSupportedType = true,
        isSupportedVariance = true
    ),
    ANNOTATION(
        displayName = "注解",
        isSupportedType = true,
        isSupportedVariance = false
    ),
    EXTENSION(
        displayName = "扩展",
        isSupportedType = true,
        isSupportedVariance = true
    )
}