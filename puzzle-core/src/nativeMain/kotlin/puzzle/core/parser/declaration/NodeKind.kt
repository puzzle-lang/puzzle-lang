package puzzle.core.parser.declaration

import puzzle.core.parser.Modifier
import puzzle.core.parser.Modifier.*


enum class NodeKind(
	val displayName: String,
	val supportedModifiers: Set<Modifier>
) {
	FUN(
		displayName = "函数",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST)
	),
	CLASS(
		displayName = "类",
		supportedModifiers = TopLevelAccessModifiers + setOf(OPEN, ABSTRACT)
	),
	UNIQUE(
		displayName = "单例类",
		supportedModifiers = TopLevelAccessModifiers
	),
	TRAIT(
		displayName = "特征",
		supportedModifiers = TopLevelAccessModifiers
	),
	STRUCT(
		displayName = "结构体",
		supportedModifiers = TopLevelAccessModifiers
	),
	ENUM(
		displayName = "枚举",
		supportedModifiers = TopLevelAccessModifiers
	),
	ANNOTATION(
		displayName = "注解",
		supportedModifiers = TopLevelAccessModifiers
	),
	EXTENSION(
		displayName = "扩展",
		supportedModifiers = TopLevelAccessModifiers
	),
	CONSTRUCTOR_FUN(
		displayName = "构造函数",
		supportedModifiers = TopLevelAccessModifiers + setOf(FILE)
	),
	MEMBER_FUN(
		displayName = "成员函数",
		supportedModifiers = MemberAccessModifiers + setOf(FINAL, OVERRIDE, OPEN, ABSTRACT)
	),
	MEMBER_CLASS(
		displayName = "成员类",
		supportedModifiers = MemberAccessModifiers + setOf(OWNER, OPEN, ABSTRACT)
	),
	MEMBER_SINGLE(
		displayName = "成员单例类",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_TRAIT(
		displayName = "成员特征",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_STRUCT(
		displayName = "成员结构体",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_ENUM(
		displayName = "成员枚举",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_ANNOTATION(
		displayName = "成员注解",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_EXTENSION(
		displayName = "成员扩展",
		supportedModifiers = MemberAccessModifiers
	),
	FUN_PARAMETER(
		displayName = "函数参数",
		supportedModifiers = setOf(VAR, ARGS)
	),
	CLASS_PARAMETER(
		displayName = "类参数",
		supportedModifiers = MemberAccessModifiers + setOf(OPEN, ABSTRACT, VAR, VAL, ARGS),
	),
	STRUCT_PARAMETER(
		displayName = "结构体参数",
		supportedModifiers = MemberAccessModifiers + setOf(VAR, VAL, IGNORE)
	),
	ENUM_PARAMETER(
		displayName = "枚举参数",
		supportedModifiers = MemberAccessModifiers + setOf(VAR, VAL, ARGS)
	),
	ANNOTATION_PARAMETER(
		displayName = "注解参数",
		supportedModifiers = setOf(VAL)
	),
	LAMBDA_PARAMETER(
		displayName = "Lambda 参数",
		supportedModifiers = emptySet()
	),
}

private val MemberAccessModifiers = setOf(PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC)

private val TopLevelAccessModifiers = setOf(PRIVATE, INTERNAL, MODULE, PUBLIC)