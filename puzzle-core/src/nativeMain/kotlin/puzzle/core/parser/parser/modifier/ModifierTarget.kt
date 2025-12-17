package puzzle.core.parser.parser.modifier

import puzzle.core.token.kinds.ModifierKind
import puzzle.core.token.kinds.ModifierKind.*

enum class ModifierTarget(
	val displayName: String,
	val supportedModifiers: Set<ModifierKind>
) {
	TOP_LEVEL_FUN(
		displayName = "函数",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST)
	),
	TOP_LEVEL_PROPERTY(
		displayName = "属性",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST, LATE, LAZY, VAR, VAL)
	),
	TOP_LEVEL_CLASS(
		displayName = "类",
		supportedModifiers = TopLevelAccessModifiers + setOf(OPEN, ABSTRACT, SEALED)
	),
	TOP_LEVEL_UNIQUE(
		displayName = "单例类",
		supportedModifiers = TopLevelAccessModifiers
	),
	TOP_LEVEL_TRAIT(
		displayName = "特征",
		supportedModifiers = TopLevelAccessModifiers + setOf(SEALED)
	),
	TOP_LEVEL_STRUCT(
		displayName = "结构体",
		supportedModifiers = TopLevelAccessModifiers
	),
	TOP_LEVEL_ENUM(
		displayName = "枚举",
		supportedModifiers = TopLevelAccessModifiers
	),
	TOP_LEVEL_ANNOTATION(
		displayName = "注解",
		supportedModifiers = TopLevelAccessModifiers
	),
	TOP_LEVEL_EXTENSION(
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
	MEMBER_PROPERTY(
		displayName = "属性",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST, LATE, LAZY, VAR, VAL)
	),
	MEMBER_CLASS(
		displayName = "成员类",
		supportedModifiers = MemberAccessModifiers + setOf(OWNER, OPEN, ABSTRACT)
	),
	MEMBER_UNIQUE(
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

private val MemberAccessModifiers = setOf<ModifierKind>(PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC)

private val TopLevelAccessModifiers = setOf<ModifierKind>(PRIVATE, INTERNAL, MODULE, PUBLIC)