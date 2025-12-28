package puzzle.core.parser.parser.parameter.type

enum class TypeTarget(
	val label: String,
	val allowsType: Boolean,
	val allowsVariance: Boolean,
) {
	FUN(
		label = "函数",
		allowsType = true,
		allowsVariance = false
	),
	PROPERTY(
		label = "属性",
		allowsType = true,
		allowsVariance = false
	),
	CLASS(
		label = "类",
		allowsType = true,
		allowsVariance = true
	),
	OBJECT(
		label = "单例对象",
		allowsType = false,
		allowsVariance = false
	),
	TRAIT(
		label = "特征",
		allowsType = true,
		allowsVariance = true
	),
	MIXIN(
		label = "混入",
		allowsType = true,
		allowsVariance = true
	),
	STRUCT(
		label = "结构体",
		allowsType = true,
		allowsVariance = true
	),
	ENUM(
		label = "枚举",
		allowsType = true,
		allowsVariance = true
	),
	ANNOTATION(
		label = "注解",
		allowsType = true,
		allowsVariance = false
	),
	EXTENSION(
		label = "扩展",
		allowsType = true,
		allowsVariance = true
	),
	TYPEALIAS(
		label = "类型别名",
		allowsType = true,
		allowsVariance = false
	),
	CTOR(
		label = "次构造函数",
		allowsType = false,
		allowsVariance = false
	),
	INIT(
		label = "初始化块",
		allowsType = false,
		allowsVariance = false
	)
}