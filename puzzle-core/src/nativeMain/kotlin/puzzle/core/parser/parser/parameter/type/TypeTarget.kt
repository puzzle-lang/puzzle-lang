package puzzle.core.parser.parser.parameter.type

enum class TypeTarget(
	val displayName: String,
	val allowsType: Boolean,
	val allowsVariance: Boolean
) {
    FUN(
        displayName = "函数",
        allowsType = true,
        allowsVariance = false
    ),
    PROPERTY(
        displayName = "属性",
        allowsType = true,
        allowsVariance = false
    ),
    CLASS(
        displayName = "类",
        allowsType = true,
        allowsVariance = true
    ),
    OBJECT(
        displayName = "单例类",
        allowsType = false,
        allowsVariance = false
    ),
    TRAIT(
        displayName = "特征",
        allowsType = true,
        allowsVariance = true
    ),
    STRUCT(
        displayName = "结构体",
        allowsType = true,
        allowsVariance = true
    ),
    ENUM(
        displayName = "枚举",
        allowsType = true,
        allowsVariance = true
    ),
    ANNOTATION(
        displayName = "注解",
        allowsType = true,
        allowsVariance = false
    ),
    EXTENSION(
        displayName = "扩展",
        allowsType = true,
        allowsVariance = true
    ),
	CTOR(
		displayName = "构造函数",
		allowsType = false,
		allowsVariance = false
	)
}